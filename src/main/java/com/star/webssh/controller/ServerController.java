package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:18
 */
@RestController
@RequestMapping("/system")
@Slf4j
public class ServerController {

    @Autowired
    private ServerService serverService;

    /**
     * 新增连接ssh信息
     * @param server
     * @return
     */

    @PostMapping("/addSSh")
    public R<String> add(@RequestBody SshServer server){

        //设置默认值为0
        server.setStatus(0);
        //server.setId((long)1);
        server.setCreate_time(LocalDateTime.now());
        server.setUpdateTime(LocalDateTime.now());
        server.setUserId(BaseContext.getCurrentId());
        Long userId = BaseContext.getCurrentId();
        server.setUserId(userId);

        //将密码进行加密
        //String password = DigestUtils.md5DigestAsHex(server.getSshPassword().getBytes());
       // server.setSshPassword(password);
        /*
        if(连接上了){
        保存到数据库里面
        }else
        {
            throw new CustomExcepion("连接失败“）
        }
         */
//        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(server.getSshName()!=null,SshServer::getSshName,server.getSshName());

//        SshServer serviceOne = serverService.getOne(lqw);
//        if(serviceOne==null){
            //如果改连接主机表中没有则添加信息
        log.info("serverInfo:{}",server);
        serverService.save(server);
            return R.success("添加成功");
//        }
//        return R.error("已有该主机，添加失败");

    }

    /**
     * 查询所有录入信息
     * @return
     */

    @GetMapping("/list")
    public R<List> list(){

        //设置条件
        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        lqw.eq(SshServer::getUserId,userId);

        List<SshServer> list = serverService.list(lqw);

//        //将密码解密后返回前端
//        List<SshServer> serverList=list.stream().map((item)->{
//            String password = DigestUtils.md5DigestAsHex(item.getSshPassword().getBytes());
//            item.setSshPassword(password);
//            return item;
//        }).collect(Collectors.toList());
        return R.success(list);
    }

    /**
     * 根据id来修改信息
     * @param sshServer
     * @return
     */

    @PutMapping("/updateSSh")
    public R<String> updateById(@RequestBody SshServer sshServer){
        //设置更新时间
        sshServer.setUpdateTime(LocalDateTime.now());

        serverService.updateById(sshServer);
        return R.success("修改成功");


    }

    /**
     * 批量删除/删除
     * @param ids
     * @return
     */

    @DeleteMapping("/ssh/{ids}")
    public R<String> deleteById(@PathVariable List<Long> ids){

        serverService.removeByIds(ids);
        return R.success("删除成功");

    }
}
