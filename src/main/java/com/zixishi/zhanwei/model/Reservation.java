package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 预约
 */
@Data
public class Reservation {
    /**
     * 预约id
     */
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate bookDate;

    private Area area;

    private Seat seat;
    /**
     * 哪个用户预约的
     */
    private User user;

    private Boolean haveUsing;

    private String cancelReason;

    private Double payAmount;

    private Double returnMoney;

    private Boolean haveClock;

    private String status;
}
