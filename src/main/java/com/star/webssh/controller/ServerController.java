package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.R;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:18
 */
@RestController
@RequestMapping("system")
@Slf4j
public class ServerController {

    @Autowired
    private ServerService serverService;

    @PostMapping("addSSh")
    public R<String> add(@RequestBody SshServer server){
        log.info("serverInfo:{}",server);

        //设置默认值为0
        server.setStatus(0);
        //server.setId((long)1);
        server.setCreate_time(LocalDateTime.now());
        server.setUpdateTime(LocalDateTime.now());
        /*
        if(连接上了){
        保存到数据库里面
        }else
        {
            throw new CustomExcepion("连接失败“）
        }
         */
        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
        lqw.eq(server.getSshName()!=null,SshServer::getSshName,server.getSshName());

        SshServer serviceOne = serverService.getOne(lqw);
        if(serviceOne==null){
            //如果改连接主机表中没有则添加信息
            serverService.save(server);
            return R.success("添加成功");
        }
        return R.error("已有该主机，添加失败");

    }
}
