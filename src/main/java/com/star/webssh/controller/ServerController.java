package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.common.SshSshdUtil;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.pojo.commonCmd;
import com.star.webssh.service.ServerService;
import com.star.webssh.service.commonCmdService;
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

    @Autowired
    private commonCmdService commonCmdService;

    /**
     * 新增连接ssh信息
     * @param server
     * @return
     */

    @PostMapping("/addSSh")
    public R<String> add(@RequestBody SshServer server){

        //设置默认值为0，表示新增为连接失败的，检查连接在list中处理
        server.setStatus(0);
        //server.setId((long)1);
        server.setCreate_time(LocalDateTime.now());
        server.setUpdateTime(LocalDateTime.now());
        server.setUserId(BaseContext.getCurrentId());
        Long userId = BaseContext.getCurrentId();
        server.setUserId((long) 1);


        log.info("serverInfo:{}",server);
        serverService.save(server);
            return R.success("添加成功");

    }

    /**
     * 根据登录用户的id查询所连接ssh
     * status代表是否检查连接情况
     * @return
     */

    @GetMapping("/list/{status}")
    public R<List<SshServer>> list(@PathVariable Integer status){

        List<SshServer> list=serverService.getList(status);
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

        LambdaQueryWrapper<commonCmd> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(commonCmd::getServerId,ids);

        commonCmdService.remove(queryWrapper);


        return R.success("删除成功");

    }


    /**
     * 通过id查询服务器信息
     * 连接ssh时查询服务器信息
     * @param id
     * @return
     */
    @GetMapping("/getSsh/{id}")
    public R<SshServer> getById(@PathVariable Long id){
        SshServer sshServer = serverService.getById(id);
        return R.success(sshServer);

    }
}
