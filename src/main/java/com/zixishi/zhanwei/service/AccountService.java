package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.util.RestResult;

import java.util.List;

public interface AccountService {
    Account findByUsername(String username);

    Account get(long id);

    Long save(Account account);

    RestResult findRoleByAccount(Account account);

    Account attach(Long id);

    Account attach();
}
