package com.zixishi.zhanwei.model;

import lombok.Data;

import java.util.List;

/**
 * 区域
 */
@Data
public class Area {

    private Long id;

    private String name;

    private Integer amount;

    private String image;
    /**
     * 该区域下有什么套餐
     */
    private List<Package> packageList;
    /**
     * 该区域下有哪些描述
     */
    private List<AreaDetail> areaDetailList;
}
