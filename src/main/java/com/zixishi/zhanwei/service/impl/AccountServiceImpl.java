package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static   Long id = null;

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public Account findByUsername(String username) {
        return accountMapper.findByUsername(username);
    }

    @Override
    public Account get(long id) {
        return null;
    }

    @Override
    public Long save(Account account) {
        accountMapper.save(account);
        List<Role> roles = roleMapper.search();
        for (Role role : roles) {
            if("管理员".equals(role.getRolename())) {
                accountMapper.bind(account,role);
            }
        }
        return  account.getId();
    }

    @Override
    public RestResult findRoleByAccount(Account account) {
       List<Role> roles =  accountMapper.findRoleByAccount(account.getId());
        return RestResult.success(roles);
    }

    @Override
    public Account attach(Long id) {
        this.id = id;
        Account account = new Account();
        account.setId(id);
        return account;
    }


    public Account attach() {
        Account account = new Account();
        account.setId(this.id);
        return account;
    }
}
