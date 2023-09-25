package com.star.webssh.dto;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mr.Wang
 * @create 2023-09-25-18:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String username;

    private String phone;

    private String code;

    private String password;
}
