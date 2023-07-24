package com.star.webssh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.webssh.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr.Wang
 * @create 2023-06-05-20:45
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
