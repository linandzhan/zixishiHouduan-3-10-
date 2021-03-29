package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class ReservationSaveDTO {
    private Long userId;

    private Long areaId;

    private Long seatId;

    private LocalDate reservationDate;


    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean using;

    private Double money;

    private Boolean haveClock;

    private String status;
}
