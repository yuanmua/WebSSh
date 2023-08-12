package com.star.webssh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.webssh.pojo.SshServer;

import java.util.List;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:21
 */
public interface ServerService extends IService<SshServer> {
    /**
     * 根据用户id查询ssh连接信息，
     * status表示是否检查连接
     * @param status
     */
    List<SshServer> getList(Integer status);
}
