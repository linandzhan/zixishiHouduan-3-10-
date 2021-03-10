package com.zixishi.zhanwei.service.impl;


import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.service.RoleService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;


    @Override
    public RestResult searchRole(String roleName, Pageable pageable) {
        List<Role> list =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->roleMapper.searchRole(roleName, pageable));
        Long total = roleMapper.count(roleName, pageable);
        ListDTO listDTO = new ListDTO();
        listDTO.setItems(list);
        listDTO.setTotal(total);
        return RestResult.success(listDTO);
    }
}
