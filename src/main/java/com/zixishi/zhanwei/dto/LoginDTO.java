package com.zixishi.zhanwei.dto;

import lombok.Data;

@Data
public class LoginDTO {
    //用户id
    private long id;

    //随机生成的uuid
    private String token;

    private String username;
}
