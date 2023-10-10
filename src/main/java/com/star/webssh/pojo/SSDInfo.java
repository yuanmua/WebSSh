package com.star.webssh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSDInfo {
    private double SSDPercent; //记录硬盘使用百分比
    private double usedSSD; //记录已用内存 单位为G
    private double allSSD; //记录总内存
    private double freeSSD; //记录剩余内存

}
