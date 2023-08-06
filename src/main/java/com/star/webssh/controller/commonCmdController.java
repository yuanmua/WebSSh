package com.star.webssh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.pojo.Employee;
import com.star.webssh.pojo.SshServer;
import com.star.webssh.pojo.commonCmd;
import com.star.webssh.service.commonCmdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cmd")
public class commonCmdController {
    @Autowired
    private commonCmdService commonCmdService;

    /**
     * 新增常用指令集
     * 需前端传输：服务器id 指令集名字 指令集内容
     * @param commonCmd
     * @return
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody commonCmd commonCmd){
        Long useId = BaseContext.getCurrentId();

        commonCmd.setUserId(useId);

        commonCmdService.save(commonCmd);

        return R.success("新增常用指令集成功");
    }

    /**
     * 删除常用指令集,未提供批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R<String> remove(@PathVariable Long ids){
        commonCmdService.removeById(ids);

        return R.success("删除常用指令集成功");
    }

    /**
     * 修改常用指令集
     * @param commonCmd
     * @return
     */
    @PutMapping("/update")
    public R<String> update(@RequestBody commonCmd commonCmd){
           commonCmdService.updateById(commonCmd);

           return R.success("修改常用指令集成功");
    }

    /**
     * 修改时的数据回显
     * 前端提供指令集id
     * @param ids
     * @return
     */
    @GetMapping("/{ids}")
    public R<commonCmd> getById(@PathVariable Long ids){
        log.info("根据id查询常用指令集信息");
        commonCmd commonCmd = commonCmdService.getById(ids);
        if(commonCmd!=null)
            return R.success(commonCmd);
        return R.error("没有查询到指令集信息");
    }

    /**
     * 根据用户id 和 服务器id 查询该用户的当前服务器下的所有指令集
     * 需前端提供服务器id
     * @param ids
     * @return
     */
    @GetMapping("/list/{ids}")
    public  R<List<commonCmd>> list (@PathVariable Long ids) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<commonCmd> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(commonCmd::getUserId,userId);
        queryWrapper.eq(commonCmd::getServerId,ids);
        List<commonCmd> list = commonCmdService.list(queryWrapper);
        return R.success(list);
    }

}
