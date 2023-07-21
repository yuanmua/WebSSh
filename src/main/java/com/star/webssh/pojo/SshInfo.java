package com.star.webssh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Mr.Wang
 * @create 2023-07-21-16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SshInfo {
    private Long id;
    private String userId;
    private String user;
    private String operator;
    private String url;
    private String localAddress;
    private String remoteAddress;
    private LocalDateTime operatorTime;
    private LocalDateTime loginTime;

}
