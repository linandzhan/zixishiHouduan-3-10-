package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.PackageMapper;
import com.zixishi.zhanwei.model.Package;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PackageController {

    @Resource
    private PackageMapper packageMapper;

    /**
     * 获取套餐信息
     * @return
     */
    @Authorization
    @ApiOperation(value = "获取套餐信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/package/search")
//    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult search() {

        return RestResult.success(packageMapper.search());
    }


    /**
     * 获取套餐信息
     * @return
     */
    @Authorization
    @ApiOperation(value = "获取单个套餐信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/package/get")
    //    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult get(@RequestBody JSONObject jsonObject) {
        String packageId = (String) jsonObject.get("id");

        Package apackage = packageMapper.get(Long.parseLong(packageId));
        return RestResult.success(apackage);
    }
}
