package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.mapper.EmployeeMapper;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Wang
 * @create 2023-06-05-20:47
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
