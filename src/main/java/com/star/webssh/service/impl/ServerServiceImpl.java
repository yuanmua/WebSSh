package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.mapper.ServerMapper;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:21
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, SshServer> implements ServerService {
}
