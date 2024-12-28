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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private JdbcTemplate jdbcTemplate; // 用于查询Hive的JdbcTemplate

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
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //将密码加密后进行检验,md5+盐
        employee.setPassword(DigestUtils.md5DigestAsHex((employee.getPassword()+PasswordSALT.PASSWORD_SALT).getBytes()));
        // 将密码加密后进行检验, md5 + 盐
        employee.setPassword(DigestUtils.md5DigestAsHex((employee.getPassword() + PasswordSALT.PASSWORD_SALT).getBytes()));

        // 构建SQL查询语句
        String sql = "SELECT * FROM employee WHERE username = ? AND password = ?";

        // 执行查询
        Employee emp = jdbcTemplate.queryForObject(
                sql,
                new Object[] { employee.getUsername(), employee.getPassword() },
                new RowMapper<Employee>() {
                    @Override
                    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Employee emp = new Employee();
                        emp.setId(rs.getLong("id"));
                        emp.setName(rs.getString("name"));
                        emp.setUsername(rs.getString("username"));
                        emp.setPassword(rs.getString("password"));
                        emp.setPhone(rs.getString("phone"));
                        emp.setSex(rs.getString("sex"));
                        emp.setIdNumber(rs.getString("id_number"));
                        emp.setStatus(rs.getInt("status"));
                        emp.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                        emp.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
                        emp.setCreateUser(rs.getLong("create_user"));
                        emp.setUpdateUser(rs.getLong("update_user"));
                        return emp;
                    }
                }
        );
        
        if(emp != null) {
            Map<String, Object> claim = new HashMap<>();
            claim.put("id", emp.getId());
            String jwt = JWTUtils.createJWT(claim);
            
            // 设置cookie
            Cookie cookie = new Cookie("Admin-Token", jwt);
            cookie.setPath("/");
            // 设置cookie的有效期，与JWT的过期时间一致（12小时）
            cookie.setMaxAge(43200);
            // 允许跨域访问
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            
            // 同时在响应中返回token
            Map<String, Object> result = new HashMap<>();
            result.put("token", jwt);
            result.put("employee", emp);
            
            return R.success(emp);
        } else {
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
