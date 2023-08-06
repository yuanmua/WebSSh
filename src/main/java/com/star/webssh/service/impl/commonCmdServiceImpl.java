package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.mapper.commonCmdMapper;
import com.star.webssh.pojo.commonCmd;
import com.star.webssh.service.commonCmdService;
import org.springframework.stereotype.Service;

@Service
public class commonCmdServiceImpl extends ServiceImpl<commonCmdMapper, commonCmd> implements commonCmdService {
}
