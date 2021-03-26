package com.zixishi.zhanwei.model;

import lombok.Data;

/**
 * 座位
 */
@Data
public class Seat {
    private Long id;

    private Area area;

    private Boolean enabled;

    private String seatName;

    private String description;

    private Boolean status;
}
