package com.star.webssh.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.*;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.pojo.commonCmd;
import com.star.webssh.service.ServerService;
import com.star.webssh.service.commonCmdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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

    @Resource
    private RedisLimitManager redisLimitManager;

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
        server.setUserId(userId);
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


    /**
     * 保存excel中的数据到服务器表中
     * @return
     */
    @PostMapping("/file")
    public R<String> saveExcel2Db(@RequestPart("file") MultipartFile multipartFile){
        //todo 1、接收数据

        //todo 2、检验数据
        String originalFilename = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        //2.1 检验传入数据格式
        String suffix = FileUtil.getSuffix(originalFilename);
        //定义可通过的文件名后缀
        final List<String> list = Arrays.asList("xls", "xlsx");
        if (!list.contains(suffix)){
            throw new CustomException("文件格式不正确");
        }
        //2.2 检验传入数据的大小
        final Long ONE_MB=1*1024*1024l;
        if(fileSize>ONE_MB){
            throw new CustomException("文件大于1MB");
        }


        //todo 3、对每一个用户进行限流处理，减小数据库服务器的压力
        Long currentId = BaseContext.getCurrentId();
        if (currentId==null){
            return R.error("NOT_LOGIN");
        }
        redisLimitManager.doRateLimit(String.valueOf(currentId));


        //todo 4、读取将excel数据文件，将其转为csv格式

        List<Map<Integer, String>> excelList = null;
        try {
            excelList = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误",e);
        }
        if(CollUtil.isEmpty(list)){
            return R.error("数据处理失败");
        }

        ArrayList<SshServer> serverList = new ArrayList<>();
        //todo 5、读出的数据存入server表中
        for (int i = 1; i < excelList.size(); i++) {
            SshServer sshServer = new SshServer();
            LinkedHashMap<Integer, String> integerStringMap = (LinkedHashMap<Integer, String>) excelList.get(i);
            sshServer.setSshName(integerStringMap.get(0));
            sshServer.setSshHost(integerStringMap.get(1));
            sshServer.setSshClass(integerStringMap.get(2));
            sshServer.setSshPort(Integer.parseInt(integerStringMap.get(3)));
            sshServer.setSshUserName(integerStringMap.get(4));
            sshServer.setSshPassword(integerStringMap.get(5));
            sshServer.setRemark(integerStringMap.get(6));
            sshServer.setUserId(currentId);
            sshServer.setUserId(currentId);
            sshServer.setUpdateTime(LocalDateTime.now());
            sshServer.setCreate_time(LocalDateTime.now());
            serverList.add(sshServer);

        }
        boolean b = serverService.saveBatch(serverList);
        if (!b){
            throw new CustomException("数据保存失败");
        }

        //6、返回
        return R.success("导入成功");
    }

    @GetMapping("/user/list")
    public String list(){

        return "{\n" +
                "    \"total\": 2,\n" +
                "    \"rows\": [\n" +
                "        {\n" +
                "            \"createBy\": \"admin\",\n" +
                "            \"createTime\": \"2023-04-23 16:11:38\",\n" +
                "            \"updateBy\": null,\n" +
                "            \"updateTime\": null,\n" +
                "            \"remark\": \"管理员\",\n" +
                "            \"userId\": 1,\n" +
                "            \"deptId\": 103,\n" +
                "            \"userName\": \"admin\",\n" +
                "            \"nickName\": \"若依\",\n" +
                "            \"email\": \"ry@163.com\",\n" +
                "            \"phonenumber\": \"15888888888\",\n" +
                "            \"sex\": \"1\",\n" +
                "            \"avatar\": \"\",\n" +
                "            \"password\": null,\n" +
                "            \"status\": \"0\",\n" +
                "            \"delFlag\": \"0\",\n" +
                "            \"loginIp\": \"123.129.121.118\",\n" +
                "            \"loginDate\": \"2023-10-07T22:40:19.000+08:00\",\n" +
                "            \"dept\": {\n" +
                "                \"createBy\": null,\n" +
                "                \"createTime\": null,\n" +
                "                \"updateBy\": null,\n" +
                "                \"updateTime\": null,\n" +
                "                \"remark\": null,\n" +
                "                \"deptId\": 103,\n" +
                "                \"parentId\": null,\n" +
                "                \"ancestors\": null,\n" +
                "                \"deptName\": \"研发部门\",\n" +
                "                \"orderNum\": null,\n" +
                "                \"leader\": \"若依\",\n" +
                "                \"phone\": null,\n" +
                "                \"email\": null,\n" +
                "                \"status\": null,\n" +
                "                \"delFlag\": null,\n" +
                "                \"parentName\": null,\n" +
                "                \"children\": []\n" +
                "            },\n" +
                "            \"roles\": [],\n" +
                "            \"roleIds\": null,\n" +
                "            \"postIds\": null,\n" +
                "            \"roleId\": null,\n" +
                "            \"admin\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"createBy\": \"admin\",\n" +
                "            \"createTime\": \"2023-04-23 16:11:38\",\n" +
                "            \"updateBy\": null,\n" +
                "            \"updateTime\": null,\n" +
                "            \"remark\": \"测试员\",\n" +
                "            \"userId\": 2,\n" +
                "            \"deptId\": 105,\n" +
                "            \"userName\": \"ry\",\n" +
                "            \"nickName\": \"若依\",\n" +
                "            \"email\": \"ry@qq.com\",\n" +
                "            \"phonenumber\": \"15666666666\",\n" +
                "            \"sex\": \"1\",\n" +
                "            \"avatar\": \"\",\n" +
                "            \"password\": null,\n" +
                "            \"status\": \"0\",\n" +
                "            \"delFlag\": \"0\",\n" +
                "            \"loginIp\": \"114.254.2.117\",\n" +
                "            \"loginDate\": \"2023-10-07T19:27:15.000+08:00\",\n" +
                "            \"dept\": {\n" +
                "                \"createBy\": null,\n" +
                "                \"createTime\": null,\n" +
                "                \"updateBy\": null,\n" +
                "                \"updateTime\": null,\n" +
                "                \"remark\": null,\n" +
                "                \"deptId\": 105,\n" +
                "                \"parentId\": null,\n" +
                "                \"ancestors\": null,\n" +
                "                \"deptName\": \"测试部门\",\n" +
                "                \"orderNum\": null,\n" +
                "                \"leader\": \"若依\",\n" +
                "                \"phone\": null,\n" +
                "                \"email\": null,\n" +
                "                \"status\": null,\n" +
                "                \"delFlag\": null,\n" +
                "                \"parentName\": null,\n" +
                "                \"children\": []\n" +
                "            },\n" +
                "            \"roles\": [],\n" +
                "            \"roleIds\": null,\n" +
                "            \"postIds\": null,\n" +
                "            \"roleId\": null,\n" +
                "            \"admin\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"code\": 200,\n" +
                "    \"msg\": \"查询成功\"\n" +
                "}";
    }

    @GetMapping("/user/deptTree")
    public String deptTree(){

        return "{\n" +
                "    \"msg\": \"操作成功\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": 100,\n" +
                "            \"label\": \"若依科技\",\n" +
                "            \"children\": [\n" +
                "                {\n" +
                "                    \"id\": 101,\n" +
                "                    \"label\": \"深圳总公司\",\n" +
                "                    \"children\": [\n" +
                "                        {\n" +
                "                            \"id\": 103,\n" +
                "                            \"label\": \"研发部门\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"id\": 104,\n" +
                "                            \"label\": \"市场部门\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"id\": 105,\n" +
                "                            \"label\": \"测试部门\"\n" +
                "                        },\n" +
                "                        {";
    }

}
