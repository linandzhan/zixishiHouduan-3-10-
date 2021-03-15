package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.util.Pageable;

import java.util.List;

public interface UserMapper {
    List<User> search(String phone, String username);

    Long count(String phone, String username);

    void updateBalance(User user);

    User find(Long id);

    void save(User user);

    User get(Long id);
}