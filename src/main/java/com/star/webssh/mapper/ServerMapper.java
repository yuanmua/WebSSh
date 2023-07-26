package com.star.webssh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.webssh.pojo.SshServer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:19
 */
@Mapper
public interface ServerMapper extends BaseMapper<SshServer> {
}
