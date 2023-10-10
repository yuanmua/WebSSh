package com.star.webssh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryInfo {
    private double memoryPercent; //记录内存使用百分比
    private double usedMemory; //记录已用内存 单位为G
    private double allMemory; //记录总内存
    private double freeMemory; //记录剩余内存
}
