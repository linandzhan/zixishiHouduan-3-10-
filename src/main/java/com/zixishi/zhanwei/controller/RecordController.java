package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "打卡记录管理")
public class RecordController {
    /**
     * 搜索当前用户打卡记录
     * @return
     */
    @Authorization
    @ApiOperation(value = "搜索当前用户打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/record/search")
//    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult search(@RequestBody Pageable pageable) {
        System.out.println(pageable);
        return null;
    }


    /**
     * 测试参数
     * @return
     */
    @Authorization
    @ApiOperation(value = "测试参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/record/param")
//    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult param(@RequestBody Integer size) {
        System.out.println(size);
        return  null;
    }
}
