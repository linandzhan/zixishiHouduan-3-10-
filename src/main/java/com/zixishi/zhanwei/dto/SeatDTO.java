package com.zixishi.zhanwei.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


@Data
public class SeatDTO {
    private Long seatId;

    private String seatName;

    private String username;

    /**
     * 当前座位正在使用为true(默认是false)
     */
    @Value("false")
    private Boolean status;

    private String description;
}
