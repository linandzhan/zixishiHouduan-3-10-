package com.zixishi.zhanwei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signinTime;


    /**
     * 签退时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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
