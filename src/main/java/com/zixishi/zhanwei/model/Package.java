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
     * 截止有效期
     */
    private LocalDate validUnitTime;
    /**
     * 套餐数量 (比如一个月，一天，一年）
     */
    private Integer amount;
    /**
     * 套餐描述
     */
    private String description;

    /**
     * 属于哪个分类
     */
    private Category category;
    /**
     * 属于哪个区域的
     */
    private List<Area> areaList;

}
