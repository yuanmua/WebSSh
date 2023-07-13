package com.star.webssh.config;

import com.star.webssh.interceptor.WebSocketInterceptor;
import com.star.webssh.websocket.WebSSHWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSSHConfigurer implements WebSocketConfigurer {
    @Autowired
    WebSSHWebSocketHandler webSSHWebSocketHandler;

    @Autowired
    private WebSSHParameter webSSHParameter;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //socket通道
        //指定处理器和路径
        webSocketHandlerRegistry.addHandler(webSSHWebSocketHandler, webSSHParameter.getWebSocketHandler())
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*");
    }
}
