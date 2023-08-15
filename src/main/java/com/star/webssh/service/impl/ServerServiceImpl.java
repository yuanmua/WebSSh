package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.SshSshdUtil;
import com.star.webssh.mapper.ServerMapper;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:21
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, SshServer> implements ServerService {
    @Override
    public List<SshServer> getList(Integer status) {
        //设置条件
        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
//        Long userId = 1L;
        lqw.eq(SshServer::getUserId,userId);

        List<SshServer> list = this.list(lqw);

        //查询出来之后

        //否则，直接返回
        if(status==1){


            //如果status=1，检查连接情况
            list=list.stream().map((item)->{
                //先将原来得状态设置为0在进行连接测
                item.setStatus(0);
                this.updateById(item);
                //测试连接
                SshSshdUtil sshdUtil = new SshSshdUtil(item.getSshHost(), item.getSshName(), item.getSshPort(), item.getSshPassword());
                boolean isTrue = sshdUtil.initialSession();
                if (isTrue){
                    //连接成功
                    item.setStatus(1);
                    //更新表
                    this.updateById(item);

                    //关闭连接
                    try {
                        sshdUtil.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return item;
            }).collect(Collectors.toList());
        }
        return list;
    }
}
