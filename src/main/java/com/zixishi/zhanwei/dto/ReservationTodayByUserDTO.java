package com.zixishi.zhanwei.dto;

import lombok.Data;

@Data
public class ReservationTodayByUserDTO {
    /**
     * 预约id
     */
    private Long value;
    /**
     * 预约描述
     */
    private String label;
}
