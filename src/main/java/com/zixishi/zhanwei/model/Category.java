package com.zixishi.zhanwei.model;

import lombok.Data;

/**
 * 分类（套餐分类
 */
@Data
public class Category {
    private Long id;

    /**
     * 属于什么类型的
     */
    private String type;

    private String name;


    private Boolean enabled;


    private String description;
}
