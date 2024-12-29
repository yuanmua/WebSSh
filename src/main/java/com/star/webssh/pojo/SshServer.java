package com.star.webssh.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SshServer {

    private String  id;
    @JsonAlias("ID")
    private Long userId;
    private String sshUserName;
    private String sshHost;
    private Integer sshPort;
    private String sshPassword;
    private String sshName;
    private String remark;
    private String sshClass;
    private Integer status;
    private String updateTime;
    private String create_time;
}
