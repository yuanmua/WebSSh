package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Mr.Wang
 * @create 2023-06-05-20:49
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    @PostMapping("/register")
    public R<String> register(@RequestBody Employee employee){
        log.info("注册");
        //将密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));

        //设置用户创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //这些字段均无用，应当在数据库中删去
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);
        employee.setName(employee.getName());
        employee.setSex("1");
        employee.setStatus(1);
        employee.setPhone("13812312312");
        employee.setIdNumber("110101199001010047");

        empService.save(employee);

        return R.success("注册成功");
    }
}
