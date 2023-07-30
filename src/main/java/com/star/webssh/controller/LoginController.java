package com.star.webssh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Wang
 * @create 2023-05-23-22:09
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmployeeService empService;

    @PostMapping
    public R login(@RequestBody Employee e) {

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(e.getUsername()!=null,Employee::getUsername,e.getUsername());

        lqw.eq(e.getPassword()!=null,Employee::getPassword,e.getPassword());
        Employee emp=empService.getOne(lqw);
        if(emp!=null){
            Map<String,Object > claim=new HashMap<>();
            claim.put("username",emp.getUsername());
            claim.put("id",emp.getId());
            claim.put("password",emp.getPassword());
            String jwt = JWTUtils.createJWT(claim);
            return R.success(jwt);
        }
        else {
            return R.error("密码错误或用户名错误");
        }

    }
}
