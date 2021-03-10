package com.zixishi.zhanwei.model;

public class PackageBalance {

    private Long id;
    /**
     * 哪个套餐
     */
    private Package aPackage;
    /**
     * 剩余多少分钟
     */
    private Double remaining;

    /**
     * 已使用多少分钟
     */
    private Double used;
}
