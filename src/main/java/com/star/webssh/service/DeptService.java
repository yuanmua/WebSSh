package com.star.webssh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Dept;

/**
* @author champion
* @description 针对表【dept】的数据库操作Service
* @createDate 2023-10-08 20:21:59
*/
public interface DeptService extends IService<Dept> {

    /**
     * 返回部门树
     *
     * @return
     */
    R deptTree();
}
