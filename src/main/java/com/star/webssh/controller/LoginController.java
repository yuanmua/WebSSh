package com.star.webssh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Wang
 * @create 2023-05-23-22:09
 */
@RestController
@RequestMapping("/employee")
public class LoginController {
    @Autowired
    private EmployeeService empService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/logout")
    public R<String> logout() {

        // 清除cookie，并且退出
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return R.success("推出成功");


    }


    @PostMapping("/login")
    public R login(@RequestBody Employee e) {

        //将密码加密后进行检验
        e.setPassword(DigestUtils.md5DigestAsHex(e.getPassword().getBytes()));
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(e.getUsername()!=null,Employee::getUsername,e.getUsername());


        //查询到当前登录用户在表中的信息
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
