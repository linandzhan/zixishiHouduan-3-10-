package com.zixishi.zhanwei.service;


import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;

public interface RoleService {

    RestResult searchRole(String roleName, Pageable p);
}
