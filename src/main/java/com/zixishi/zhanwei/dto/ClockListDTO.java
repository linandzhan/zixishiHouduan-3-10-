package com.zixishi.zhanwei.dto;

import com.zixishi.zhanwei.model.Evaluate;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ClockListDTO {
    private Long id;

    private LocalDateTime signinTime;

    private LocalDateTime endTime;

    private Long length;

    private String seatName;

    private Evaluate evaluate;
}
