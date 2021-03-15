package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.UserService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public RestResult search(String phone, String username, Pageable pageable) {
        List<User> users =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->userMapper.search(phone, username));
        Long total = userMapper.count(phone, username);

        ListDTO listDTO = new ListDTO();
        listDTO.setItems(users);
        listDTO.setTotal(total);

        return RestResult.success(listDTO);
    }


    public User attach(Long id) {
        return userMapper.find(id);
    }

    @Override
    public void save(User user) {
        Account account = new Account();
        account.setUsername(user.getUsername());
        account.setPhone(user.getPhone());
        account.setRole("user");
        account.setPassword("123456");
        accountMapper.save(account);

        List<Role> roles = roleMapper.search();
        for (Role role : roles) {
            if("用户".equals(role.getRolename())){
                accountMapper.bind(account,role);
            }
        }


        user.setId(account.getId());
        user.setCreateTime(LocalDateTime.now());
        userMapper.save(user);
    }
}
