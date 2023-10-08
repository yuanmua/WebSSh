package com.star.webssh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDTO {
    private String ip;
    private String user;
    private String password;
}
