package com.star.webssh.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 常用指令类
 */
@Data

public class commonCmd {
    private Long id;

    private Long userId;//用户id

    private Long serverId;//服务器id

    private String name;//常用指令组的组名

    private String cmds;//常用指令组

    private String remarks;//备注

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
