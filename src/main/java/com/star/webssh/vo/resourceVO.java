package com.star.webssh.vo;

import com.star.webssh.pojo.MemoryInfo;
import com.star.webssh.pojo.SSDInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class resourceVO {
    private double cpu; //记录cpu使用百分比
    private MemoryInfo memoryInfo;//内存对象
    private SSDInfo ssdInfo; //磁盘对象




}
