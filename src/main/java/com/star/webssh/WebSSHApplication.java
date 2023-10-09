package com.star.webssh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ServletComponentScan
@MapperScan("com.star.webssh.mapper")
public class WebSSHApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSSHApplication.class);
    }
}