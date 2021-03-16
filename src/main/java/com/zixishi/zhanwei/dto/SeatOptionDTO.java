package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.util.concurrent.BlockingDeque;

@Data
public class SeatOptionDTO {
    private String label;

    private String value;

    private Boolean disabled;
}
