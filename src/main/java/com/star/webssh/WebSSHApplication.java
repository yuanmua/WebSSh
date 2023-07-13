package com.star.webssh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WebSSHApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSSHApplication.class);
    }
}