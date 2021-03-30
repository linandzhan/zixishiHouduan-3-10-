package com.zixishi.zhanwei.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 套餐
 */
@Data
public class Package {

    private Long id;
    /**
     *
     * 套餐名称
     */
    private String name;

    /**
     * 该套餐对应的价格
     */
    private Double price;

    /**
     * 套餐数量 (比如一个月，一天，一年）
     */
    private Integer amount;
    /**
     * 套餐描述
     */
    private String description;
    /**
     * 单位（年，月，日）
     */
    private String unit;

}
