package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
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
    private LocalDateTime endTime;

    /**
     * 学习时长（以分钟为单位）
     */
    private Long length;

    /**
     * 对哪个预约进行签到打卡
     */
    private Reservation reservation;
    /**
     * 是否对其进行评价
     */
    private Boolean haveComment;




}
