package com.zixishi.zhanwei.model;

import java.time.LocalDateTime;

public class Clock {
    private Long id;


    /**
     * 在哪个座位上打卡
     */
    private Seat seat;

    /**
     * 打卡用户
     */
    private User user;

    /**
     * 签到时间
     */
    private LocalDateTime signinTime;


    /**
     * 签退时间
     */
    private LocalDateTime signoutTime;

    /**
     * 结算状态 （根据该用户的套餐进行结算）
     */
    private String settleStatus;




}
