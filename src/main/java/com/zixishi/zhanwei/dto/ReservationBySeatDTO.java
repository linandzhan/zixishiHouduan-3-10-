package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ReservationBySeatDTO {
    private String username;

    private LocalTime startTime;

    private LocalTime endTime;
}
