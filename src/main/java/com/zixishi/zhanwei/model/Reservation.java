package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约
 */
@Data
public class Reservation {
    /**
     * 预约id
     */
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDate date;

    private Area area;

    private Seat seat;
    /**
     * 哪个用户预约的
     */
    private User user;
}
