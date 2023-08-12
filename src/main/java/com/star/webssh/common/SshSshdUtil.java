package com.star.webssh.common;


import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author wangzonghui
 * @date 2022年2月7日 上午9:55:58 
 * @Description ssh工具类测试
 */
public final class SshSshdUtil {

    private  final static Logger log = LoggerFactory.getLogger(SshSshdUtil.class);

    private String host;
    private String user;
    private String password;
    private int port;

    private ClientSession session;
    private SshClient client;

    /**
     * 创建一个连接
     *
     * @param host     地址
     * @param user     用户名
     * @param port     ssh端口
     * @param password 密码
     */
    public SshSshdUtil(String host, String user, int port, String password) {
        this.host = host;
        this.user = user;
        this.port=port;
        this.password=password;
    }

    /**
     * 登录
     * @return
     * @throws Exception
     */
    public boolean initialSession() {
        if (session == null) {
            try {
                // 创建 SSH客户端
                client = SshClient.setUpDefaultClient();
                // 启动 SSH客户端
                client.start();
                // 通过主机IP、端口和用户名，连接主机，获取Session
                session = client.connect(user, host, port).verify().getSession();
                // 给Session添加密码
                session.addPasswordIdentity(password);
                // 校验用户名和密码的有效性
                return session.auth().verify().isSuccess();

            } catch (Exception e) {
                log.info("Login Host:"+host+" Error",e);
                return false;
            }
        }

        System.out.println("登陆成功");

        return true;
    }

    /**
     * 关闭连接
     * @throws Exception
     */
    public void close() throws Exception  {
        //关闭session
        if (session != null && session.isOpen()) {
            session.close();
        }
        // 关闭 SSH客户端
        if (client != null && client.isOpen()) {
            client.stop();
            client.close();
        }
    }


}
