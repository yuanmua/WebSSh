package com.star.webssh.dto;/**
 * @author Mr.Wang
 * @create 2023-10-09-17:16
 */

import com.star.webssh.pojo.Dept;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName DeptDTO
 *@Description TODO
 *@Author Mr.Wang
 *@Date 2023/10/9 17:16
 *@Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptDTO  extends Dept {
    private List<DeptDTO> children;
}


