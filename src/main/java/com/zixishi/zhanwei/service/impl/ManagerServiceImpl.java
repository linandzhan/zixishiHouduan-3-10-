package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.ManagerMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.ManagerService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private ManagerMapper managerMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public RestResult search(Long roleId, Manager manager, Pageable pageable) {
        if(manager.getEnabled() != null) {
            System.out.println("sss");
        }
        //需要用分页插件，进行分页查询
        List<Manager> list =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->managerMapper.search(roleId, manager, pageable));
        Long total = managerMapper.count(roleId, manager, pageable);
        ListDTO listDTO = new ListDTO();
        listDTO.setItems(list);
        listDTO.setTotal(total);
        return RestResult.success(listDTO);
    }

    @Override
    public void save(Manager manager) {
        Map<String,Object> map = new HashMap<>();
        map.put("manager",manager);
        managerMapper.save(map);
    }

    @Override
    public RestResult addRole(List<Integer> roleIds, Integer userId) {
        roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        //先把所有关联的删掉，再重新进行添加
        accountMapper.deleteByRoles(Long.parseLong(userId.toString()));

        for (Integer roleId : roleIds) {
            managerMapper.addRole(Long.parseLong(roleId.toString()),Long.parseLong(userId.toString()));
        }
        System.out.println("分支测试");


        return RestResult.success();
    }


}
