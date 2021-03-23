package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "打卡记录管理")
public class ClockController {
    @Resource
    private ClockMapper clockMapper;

    @ApiOperation(value = "查询打卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @Authorization
    @PostMapping("/clock/searchUncoment")
//    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult searchUncoment(@RequestBody JSONObject jsonObject) {
        List<Clock> clockList = clockMapper.search();
        System.out.println(clockList);
        return null;
    }
}
