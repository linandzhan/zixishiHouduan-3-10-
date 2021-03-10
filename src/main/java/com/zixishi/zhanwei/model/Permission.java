package com.zixishi.zhanwei.model;

import lombok.Data;

@Data
public class Permission {
    private Long id;

    private String path;

    private Boolean free;
}
