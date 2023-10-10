package com.star.webssh.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import com.star.webssh.dto.DeptDTO;
import com.star.webssh.pojo.Dept;
import com.star.webssh.service.DeptService;
import com.star.webssh.mapper.DeptMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author champion
 * @description 针对表【dept】的数据库操作Service实现
 * @createDate 2023-10-08 20:21:59
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept>
        implements DeptService {

    @Override
    public R deptTree() {
//        获取当前用户的id

        Long userId = BaseContext.getCurrentId();
        //查询部门表中的leaderId为当前用户id的所有部门
//        LambdaQueryWrapper<Dept> lwq = new LambdaQueryWrapper<>();
//        lwq.eq(userId != null, Dept::getLeaderId, userId);
        List<Dept> list = this.list();
        //将查出来的数据封装到DTO中
        ArrayList<DeptDTO> dtoList = new ArrayList<>();
        for (Dept dept : list) {
            DeptDTO deptDTO = new DeptDTO();
            BeanUtil.copyProperties(dept, deptDTO);
            dtoList.add(deptDTO);
        }
//        //查询部门中的所有子部门,直到id不再为其他元组的fatherId为止
//
//        getChildrenTree(dtoList);


        /**
         * 将所有部门传入；用于构建整体的一颗部门树，每个用户都能看到，
         */
        //todo 但是关于权限问题又sercurity来处理
        List<DeptDTO> deptDTOS = buildDeptTree(dtoList);


        return R.success(deptDTOS);
    }

    private void getChildrenTree(List<DeptDTO> dtoList) {


        for (DeptDTO deptDTO : dtoList) {
            LambdaQueryWrapper<Dept> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(Dept::getFatherId, deptDTO.getId());
            List<Dept> list1 = this.list(lqw2);
            //
            List<DeptDTO> listlDTO = new ArrayList<>();
            for (Dept dept : list1) {
                DeptDTO deptDTO1 = new DeptDTO();
                BeanUtil.copyProperties(dept, deptDTO1);
                listlDTO.add(deptDTO);
            }
            //
            if (listlDTO != null) {
                deptDTO.setChildren(listlDTO);
                getChildrenTree(listlDTO);
                return;
            }else {
                return;
            }


        }
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */

    public List<DeptDTO> buildDeptTree(List<DeptDTO> depts)
    {
        List<DeptDTO> returnList = new ArrayList<DeptDTO>();
        List<Long> tempList = depts.stream().map(DeptDTO::getId).collect(Collectors.toList());
        for (DeptDTO dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getFatherId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptDTO> list, DeptDTO t)
    {
        // 得到子节点列表
        List<DeptDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptDTO tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    /**
     * 得到子节点列表
     */
    private List<DeptDTO> getChildList(List<DeptDTO> list, DeptDTO t)
    {
        List<DeptDTO> tlist = new ArrayList<DeptDTO>();
        Iterator<DeptDTO> it = list.iterator();
        while (it.hasNext())
        {
            DeptDTO n = (DeptDTO) it.next();
            if ((n.getFatherId()!=null) && n.getFatherId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }
    private boolean hasChild(List<DeptDTO> list, DeptDTO t)
    {
        return getChildList(list, t).size() > 0;
    }

}
