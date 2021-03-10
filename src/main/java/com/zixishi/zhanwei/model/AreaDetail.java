package com.zixishi.zhanwei.model;


import lombok.Data;

import java.util.List;

/**
 * 描述区域的细节
 */
@Data
public class AreaDetail {
    private Long id;

    private String detail;

    /**
     * 该描述适用于哪些区域
     */
    private List<Area> areaList;
}
