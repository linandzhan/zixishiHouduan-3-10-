package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class ReservationListDTO {
    private Long clockId;

    private String username;


    private String seatName;

    private LocalDate bookDate;


    private LocalTime  startTime;

    private LocalTime endTime;

    private Boolean haveComment;
}
