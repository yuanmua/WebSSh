package com.star.webssh.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.dto.DeptDTO;
import com.star.webssh.pojo.Dept;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.DeptService;
import com.star.webssh.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 *@ClassName DeptController
 *@Description TODO
 *@Author Mr.Wang
 *@Date 2023/10/8 20:24
 *@Version 1.0
 */
@RestController
//@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/add")

    public R<String> addDept(@RequestBody Dept dept){
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        dept.setCreateId(BaseContext.getCurrentId());
        deptService.save(dept);
        return R.success("成功");
    }

    /**
     * 将该用户所管理的部门全部返回
     * @return
     */
    @GetMapping("/system/user/deptTree")
    public R<DeptDTO> deptTree() {


        return deptService.deptTree();
    }
    //查询所有员工
    //为分页查询设置默认参数，如果前端不传过来值，就用默认参数
//    @GetMapping("/user/list/{deptId}")
    @GetMapping("/system/user/list")
//    //@PathVariable Long deptId,
    public R<Page<Employee>> list(@RequestParam(defaultValue = "1") Long pageNum, @RequestParam(defaultValue = "10") Long pageSize){

        Page<Employee> employeePage = new Page<>(pageNum,pageSize);

        Page<Employee> page = employeeService.page(employeePage);
        return R.success(page);
    }

}


