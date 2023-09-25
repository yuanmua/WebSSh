package com.star.webssh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.webssh.common.R;
import com.star.webssh.dto.LoginDTO;
import com.star.webssh.pojo.Employee;


/**
 * @author Mr.Wang
 * @create 2023-06-05-20:46
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 发短信
     * @param phone
     * @return
     */
    R<String> sendCode(String phone);

    /**
     * 短信登陆
     * @param loginDTO
     */
    R loginWithCode(LoginDTO loginDTO);

    /**
     * 注册用户
     * @param loginDTO
     * @return
     */
    R<String> register(LoginDTO loginDTO);
}
