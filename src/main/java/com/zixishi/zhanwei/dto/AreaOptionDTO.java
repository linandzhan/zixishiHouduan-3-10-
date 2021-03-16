package com.zixishi.zhanwei.dto;

import lombok.Data;

import java.util.List;

@Data
public class AreaOptionDTO {
    private String label;

    private String value;

    private List<SeatOptionDTO> children;
}
