package com.star.webssh.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("com.star.webssh")
public class WebSSHParameter {
    private String webSocketHandler;
}
