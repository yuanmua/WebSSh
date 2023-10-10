package com.star.webssh.controller;

import com.star.webssh.common.LinuxStateForShell;
import com.star.webssh.common.R;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import com.star.webssh.vo.resourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/monitor")
public class SystemMonitorController {
    /**
     * 需前端提供远端服务器的IP 用户名 密码 ，后端将返回形如
     * CPU 用户使用占有率:  0.0 %
     * 系统物理内存使用情况:   2976.4 k 总计,    193.6 k 空闲,   1213.9 k 已使用,   1568.9 k 缓存
     * 系统磁盘状态:大小 70G , 已使用27G ,空闲43G  的字符串
     * @param
     * @return
     */
    @Autowired
    private ServerService serverService;

    @PostMapping()
    public R<resourceVO> systemMonitor(@RequestBody Long id){
        SshServer sshServer = serverService.getById(id);
        String ip=sshServer.getSshHost();
        String user=sshServer.getSshUserName();
        String password=sshServer.getSshPassword();
        log.info("系统检测开始");
        Map result=null;
        double cpu=0;
        resourceVO resourceVO=null;
        while (cpu==0){
            result=null;
            while(result==null){
                result = LinuxStateForShell.runDistanceShell(LinuxStateForShell.COMMANDS, user, password, ip);

            }

            resourceVO=LinuxStateForShell.disposeResultMessage(result);
            cpu=resourceVO.getCpu();
        }


        return R.success(resourceVO);
    }



}
