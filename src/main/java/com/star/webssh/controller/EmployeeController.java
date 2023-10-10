package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import com.star.webssh.constant.PasswordSALT;
import com.star.webssh.dto.LoginDTO;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private EmployeeService empService;

    @PostMapping("/register")
    public R<String> register(@RequestBody LoginDTO loginDTO){

        return empService.register(loginDTO);

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
     * 账户密码登录
     * @param e
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Employee e) {

        //将密码加密后进行检验,md5+盐
        e.setPassword(DigestUtils.md5DigestAsHex((e.getPassword()+PasswordSALT.PASSWORD_SALT).getBytes()));
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(e.getUsername()!=null,Employee::getUsername,e.getUsername());

        //查询到当前登录用户在表中的信息
        lqw.eq(e.getPassword()!=null,Employee::getPassword,e.getPassword());
        Employee emp=empService.getOne(lqw);
        if(emp!=null){
            Map<String,Object > claim=new HashMap<>();
            claim.put("id",emp.getId());
            String jwt = JWTUtils.createJWT(claim);

            return R.success(jwt);
        }
        else {
            return R.error("密码错误或用户名错误");
        }

    }

    /**
     * 发送手机验证码
     */
    @PostMapping("/code")
    public R<String> sendCode(@RequestParam("phone") String phone) {
        //  发送短信验证码并保存验证码
        return empService.sendCode(phone);
    }

    /**
     * 短信登陆
     * @param loginDTO
     */
    @PostMapping("/loginWithCode")
    public R<String> loginWithCode(@RequestBody LoginDTO loginDTO){

        return empService.loginWithCode(loginDTO);

    }
}
