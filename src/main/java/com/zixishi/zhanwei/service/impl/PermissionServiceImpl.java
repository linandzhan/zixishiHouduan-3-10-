package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.PermissionMapper;
import com.zixishi.zhanwei.model.Permission;
import com.zixishi.zhanwei.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Set<String> findByUser(long userId) {
        return permissionMapper.findByUser(userId);
    }

    @Override
    public Boolean findByPath(String path) {
        return permissionMapper.findByPath(path);
    }
}
