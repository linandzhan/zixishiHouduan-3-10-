package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
public class Order {

    private Long id;
    /**
     * 该订单定了，哪个套餐
     */
    private Package Package;

    /**
     * 谁订的
     */
    private User user;

    /**
     *购买数量 （只能买一个套餐，一个套餐可以有多个数量）
     */
    private Integer amount;

    /**
     * 订单状态 (未付款，已付款，退款）
     */
    private String status;

    private LocalDateTime createTime;

    private LocalDateTime paymentTime;

    private Double paymentAmount;
}
