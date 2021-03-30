package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购买套餐记录表
 */
@Data
public class packageOrder {
    private Long id;

    private User userId;

    private Package packageId;

    private LocalDateTime createTime;

    private Double payAmount;
}
