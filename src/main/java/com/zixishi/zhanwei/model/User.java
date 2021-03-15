package com.zixishi.zhanwei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {

    private Long id;

    private String username;

    private String phone;

    private String avator;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String gender;
    /**
     * 用户的套餐余额
     */
    private List<PackageBalance> packageBalanceList;

    private Double balance;


    private String password;
}
