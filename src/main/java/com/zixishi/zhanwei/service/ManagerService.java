package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;

import java.util.List;

public interface ManagerService {
    RestResult search(Long roleId, Manager manager, Pageable pageable);


    void save(Manager manager);

    RestResult addRole(List<Integer> roleIds, Integer userId);
}
