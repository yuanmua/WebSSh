package com.star.webssh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.mapper.ServerMapper;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.Wang
 * @create 2023-07-26-23:21
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, SshServer> implements ServerService {
    @Autowired
    private ServerMapper serverMapper;

    @Override
    public boolean save(SshServer server) {
        serverMapper.insertSshServer(server);

        return false;
    }

    @Override
    public List<SshServer> getList(Integer status) {
        return serverMapper.selectSshServersByStatus(status);
    }

    @Override
    public SshServer getById(Long id) {
        return serverMapper.selectSshServerById(id);
    }

    //    @Override
//    public List<SshServer> getList(Integer status) {
//        //设置条件
//        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
//        Long userId = BaseContext.getCurrentId();
////        Long userId = 1L;
//        lqw.eq(SshServer::getUserId,userId);
//
//        List<SshServer> list = this.list(lqw);
//
//        //查询出来之后
//
//        //否则，直接返回
//        if(status==1){
//
//
//            //如果status=1，检查连接情况
//            list=list.stream().map((item)->{
//                //先将原来得状态设置为0在进行连接测
//                item.setStatus(0);
//                this.updateById(item);
//                //测试连接
//                SshSshdUtil sshdUtil = new SshSshdUtil(item.getSshHost(), item.getSshUserName(), item.getSshPort(), item.getSshPassword());
//                boolean isTrue = sshdUtil.initialSession();
//                if (isTrue){
//                    //连接成功
//                    item.setStatus(1);
//                    //更新表
//                    this.updateById(item);
//
//                    //关闭连接
//                    try {
//                        sshdUtil.close();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                return item;
//            }).collect(Collectors.toList());
//        }
//        return list;
//    }
//
//    public List<SshServer> getList(Long data,Integer status) {
//        //设置条件
//        LambdaQueryWrapper<SshServer> lqw = new LambdaQueryWrapper<>();
//        Long userId = data;
////        Long userId = 1L;
//        lqw.eq(SshServer::getUserId,userId);
//
//        List<SshServer> list = this.list(lqw);
//
//        //查询出来之后
//
//        //否则，直接返回
//        if(status==1){
//
//
//            //如果status=1，检查连接情况
//            list=list.stream().map((item)->{
//                //先将原来得状态设置为0在进行连接测
//                item.setStatus(0);
//                this.updateById(item);
//                //测试连接
//                SshSshdUtil sshdUtil = new SshSshdUtil(item.getSshHost(), item.getSshUserName(), item.getSshPort(), item.getSshPassword());
//                boolean isTrue = sshdUtil.initialSession();
//                if (isTrue){
//                    //连接成功
//                    item.setStatus(1);
//                    //更新表
//                    this.updateById(item);
//
//                    //关闭连接
//                    try {
//                        sshdUtil.close();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                return item;
//            }).collect(Collectors.toList());
//        }
//        return list;
//    }

}
