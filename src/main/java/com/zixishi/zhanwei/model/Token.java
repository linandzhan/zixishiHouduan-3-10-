package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Token {
    private Long id;

    private String token;

    private LocalDateTime createTime;

    private Account account;
}
