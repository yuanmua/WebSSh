package com.star.webssh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName dept
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dept implements Serializable {
    /**
     * 部门id
     */
    private Long id;

    /**
     * 
     */
    private String deptName;

    /**
     * 部门负责任的id
     */
    private String leaderId;

    /**
     * 状态：0禁用，1：启用
     */
    private Integer status;

    /**
     * 
     */
    private Long createId;

    /**
     *
     */
    private Long fatherId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 
     */
    private Long updateUserid;



    private static final long serialVersionUID = 1L;


}