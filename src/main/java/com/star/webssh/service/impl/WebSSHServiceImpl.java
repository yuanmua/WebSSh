package com.star.webssh.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.star.webssh.constant.ConstantPool;
import com.star.webssh.pojo.SSHConnectInfo;
import com.star.webssh.pojo.WebSSHData;
import com.star.webssh.service.WebSSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebSSH业务逻辑实现
 */
@Service
public class WebSSHServiceImpl implements WebSSHService {
    //存放ssh连接信息的map
    private static Map<String, Object> sshMap = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 初始化ssh连接
     *
     * @param session /
     */
    @Override
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setjSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        //将这个ssh连接信息放入map中
        sshMap.put(uuid, sshConnectInfo);
    }

    /**
     * 处理客户段发的数据
     *
     * @param buffer  /
     * @param session /
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHData webSSHData = null;
        try {
            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
        } catch (IOException e) {
            logger.error("Json转换异常");
            logger.error("异常信息:{}", e.getMessage());
            return;
        }
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            //启动线程异步处理
            WebSSHData finalWebSSHData = webSSHData;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(sshConnectInfo, finalWebSSHData, session);
                    } catch (JSchException | IOException e) {
                        logger.error("webssh连接异常");
                        logger.error("异常信息:{}", e.getMessage());
                        close(session);
                    }
                }
            });
        } else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            String command = webSSHData.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    transToSSH(sshConnectInfo.getChannel(), command);
                } catch (IOException e) {
                    logger.error("webssh连接异常");
                    logger.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            }
        } else {
            logger.error("不支持的操作");
            close(session);
        }
    }

    /**
     * 数据写回前端 for websocket
     *
     * @param session /
     * @param buffer  /
     * @throws IOException /
     */
    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            //map中移除
            sshMap.remove(userId);
        }
    }

    /**
     * 使用jsch连接终端
     *
     * @param sshConnectInfo   /
     * @param webSSHData       /
     * @param webSocketSession /
     * @throws JSchException /
     * @throws IOException   /
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = null;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);

        //开启shell通道
        Channel channel = session.openChannel("shell");

        //通道连接 超时时间3s
        channel.connect(3000);

        //设置channel
        sshConnectInfo.setChannel(channel);

        //转发消息
        transToSSH(channel, "\r");

        //读取终端返回的信息流
        try (InputStream inputStream = channel.getInputStream()) {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
        }
    }

    /**
     * 将消息转发到终端
     *
     * @param channel /
     * @param command /
     * @throws IOException /
     */
    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
