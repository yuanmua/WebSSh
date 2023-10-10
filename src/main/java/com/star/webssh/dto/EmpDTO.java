package com.star.webssh.dto;/**
 * @author Mr.Wang
 * @create 2023-10-08-20:52
 */

import com.star.webssh.pojo.Dept;
import com.star.webssh.pojo.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@ClassName ServerDto
 *@Description TODO
 *@Author Mr.Wang
 *@Date 2023/10/8 20:52
 *@Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpDTO extends Employee{

    Dept dept;
}


