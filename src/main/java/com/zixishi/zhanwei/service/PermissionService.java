package com.zixishi.zhanwei.service;


import java.util.Set;

public interface PermissionService {
    Set<String> findByUser(long userId);

    Boolean findByPath(String path);
}
