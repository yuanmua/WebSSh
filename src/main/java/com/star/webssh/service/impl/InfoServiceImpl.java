package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.mapper.InfoMapper;
import com.star.webssh.pojo.SshInfo;
import com.star.webssh.service.InfoService;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Wang
 * @create 2023-07-21-17:44
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, SshInfo> implements InfoService {
}
