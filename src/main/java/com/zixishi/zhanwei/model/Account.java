package com.zixishi.zhanwei.model;

import lombok.Data;

@Data
public class Account {
    private Long id;
    /**
     * 该账号是什么角色的（用户，管理员，超级管理员）
     */
    private String type;

    private String username;

    private String phone;

    private String password;
}
