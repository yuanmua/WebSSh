package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 登录
     *
     * @param request
     * @param emp
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee emp) {

        System.out.println(emp);
        //1 将页面提交的用户名username进行md5加密处理
        String password = emp.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2根据页面提交用户名username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, emp.getUsername());
        Employee emp1 = empService.getOne(lqw);
        //3如果没有查到返回登陆失败结果
        if (emp1 == null) {
            System.out.println("登陆失败1");
            System.out.println("登陆失败1");
            return R.error("登陆失败");
        }
        //4进行密码比对
        if (!emp1.getPassword().equals(password)) {
            System.out.println("登陆失败2");
            return R.error("登陆失败");
        }
        //5 查看员工状态，如果是已禁用，返回已禁用结果
        if (emp1.getStatus() == 0) {
            return R.error("员工已禁用");
        }
        //6 登陆成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee", emp1.getId());

        return R.success(emp1);
    }

    /**
     * 退出登录操作
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理本地浏览器Seccess中保存的当前登陆员工的id信息
        //Attribute n:属性
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping("/register")
    public R<String> register(@RequestBody Employee employee){
        log.info("注册");
        //将密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));

        //设置用户创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //这些字段均无用，应当在数据库中删去
        employee.setCreateUser((long)1);
        employee.setUpdateUser((long)1);
        employee.setName("这个字段应该删去");
        employee.setSex("1");
        employee.setStatus(1);
        employee.setPhone("13812312312");
        employee.setIdNumber("110101199001010047");

        empService.save(employee);

        return R.success("注册成功");
    }
}
