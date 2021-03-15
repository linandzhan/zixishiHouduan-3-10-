package com.zixishi.zhanwei.model;

import lombok.Data;

import java.util.List;

@Data
public class Router {
    private Long id;

    private String displayName;

    private String icon;

    private Integer mid;

    private List<Router> children;


    private String link;
}
