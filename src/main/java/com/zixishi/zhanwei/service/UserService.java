package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;

public interface UserService {
    RestResult search(String phone, String username, Pageable pageable);
}
