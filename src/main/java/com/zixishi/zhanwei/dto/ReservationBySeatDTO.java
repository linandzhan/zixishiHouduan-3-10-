package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ReservationBySeatDTO {
    private Long id;

    private String username;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean haveUsing;

    private String cancelReason;
}
