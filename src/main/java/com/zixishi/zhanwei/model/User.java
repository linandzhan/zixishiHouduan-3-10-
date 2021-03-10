package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {

    private Long id;

    private String username;

    private String phone;

    private String avator;

    private LocalDateTime createTime;

    private String gender;
    /**
     * 用户的套餐余额
     */
    private List<PackageBalance> packageBalanceList;
}
