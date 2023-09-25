package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Wang
 * @create 2023-06-05-20:49
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

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

    /**
     * 退出登录
     * @return
     */

    @PostMapping("/logout")
    public R<String> logout() {

        // 清除cookie，并且退出
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return R.success("推出成功");


    }


    /**
     * 登录
     * @param e
     * @return
     */
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
