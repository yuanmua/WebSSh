package com.star.webssh.websocket;

import com.star.webssh.constant.ConstantPool;
import com.star.webssh.pojo.SshInfo;
import com.star.webssh.service.InfoService;
import com.star.webssh.service.WebSSHService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;

/**
 * WebSSH的WebSocket处理器
 */
@Component
public class WebSSHWebSocketHandler implements WebSocketHandler {
    @Autowired
    private WebSSHService webSSHService;

    @Autowired
    private InfoService infoService;
    private Logger logger = LoggerFactory.getLogger(WebSSHWebSocketHandler.class);

    /**
     * 用户连接上WebSocket的回调
     * @param webSocketSession  /
     * @throws Exception    /
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("用户:{},连接WebSSH", webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY));
        logger.info(webSocketSession.getId());
        logger.info(webSocketSession.getUri().toString());

        //调用初始化连接
        webSSHService.initConnection(webSocketSession);
        //创建日志对象

        SshInfo operatorInfo=getInfo(webSocketSession,"连接WebSSH");
        infoService.save(operatorInfo);

    }

    /**
     * 收到消息的回调
     * @param webSocketSession  /
     * @param webSocketMessage  /
     * @throws Exception    /
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
            logger.info("用户:{},发送命令:{}", webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY), webSocketMessage.toString());
            //调用service接收消息
            webSSHService.recvHandle(((TextMessage) webSocketMessage).getPayload(), webSocketSession);

            //创建日志对象

            SshInfo operatorInfo=getInfo(webSocketSession,webSocketMessage.toString());
            infoService.save(operatorInfo);
        } else if (webSocketMessage instanceof BinaryMessage) {

        } else if (webSocketMessage instanceof PongMessage) {

        } else {
            System.out.println("Unexpected WebSocket message type: " + webSocketMessage);
        }
    }

    /**
     * 出现错误的回调
     * @param webSocketSession  /
     * @param throwable /
     * @throws Exception    /
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        logger.error("数据传输错误");
    }

    /**
     * 连接关闭的回调
     * @param webSocketSession  /
     * @param closeStatus   /
     * @throws Exception    /
     */
    @Override

    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        //调用service关闭连接
        webSSHService.close(webSocketSession);
        //创建日志对象

        SshInfo operatorInfo=getInfo(webSocketSession,"断开webssh连接");
        infoService.save(operatorInfo);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 日志函数
     * @param webSocketSession
     * @param massage
     * @return
     */
    public  SshInfo getInfo(WebSocketSession webSocketSession,String massage){
        String user = webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY).toString();
        String url = webSocketSession.getUri().toString();
        String userId = webSocketSession.getId();

        String localAddress = webSocketSession.getLocalAddress().getAddress().toString();
        String remoteAddress = webSocketSession.getRemoteAddress().toString();
        return  new SshInfo(null,userId, user,massage, url,localAddress, remoteAddress, LocalDateTime.now(), LocalDateTime.now());
    }
}

