package com.star.webssh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.webssh.pojo.Dept;

import org.apache.ibatis.annotations.Mapper;

/**
* @author champion
* @description 针对表【dept】的数据库操作Mapper
* @createDate 2023-10-08 20:21:59
* @Entity com.star.webssh.pojo.Dept
*/
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {


}
