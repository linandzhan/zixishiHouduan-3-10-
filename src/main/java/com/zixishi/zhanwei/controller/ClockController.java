package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.service.ClockService;
import com.zixishi.zhanwei.util.HttpResult;
import com.zixishi.zhanwei.util.HttpUtil;
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
    @Resource
    private ClockService clockService;

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


    @ApiOperation(value = "检查是签到还是签退还是已经签退打卡结束了")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/clock/checkSignInOrOut")
//    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult checkSignInOrOut(@RequestBody JSONObject jsonObject) {
        Integer id = (Integer) jsonObject.get("reservation");

        return  clockService.checkSignInOrOut(Long.parseLong(id.toString()));
    }

    @ApiOperation(value = "获取打卡二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/clock/getErWeiMa")
    //    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult getErWeiMa(@RequestBody JSONObject jsonObject) {
        HttpResult httpResult = HttpUtil.doGet("https://api.gugudata.com/barcode/qrcode?appkey=BLPTTYNNCYA4&content=http://192.168.0.47:8088/%23/clock/signInOut&size=500", null);
        String body = httpResult.getBody();
        JSONObject parse = (JSONObject) JSON.parse(body);
        JSONObject data = (JSONObject) parse.get("Data");
        String url = (String) data.get("Url");
        System.out.println(url);
        return RestResult.success("返回成功",url);
    }


    @ApiOperation(value = "打卡操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/clock/sign")
    public RestResult sign(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        Integer reservationId = (Integer) jsonObject.get("reservation");
        String status = (String) jsonObject.get("status");
        String result = clockService.sign(Long.parseLong(reservationId.toString()),status);
        return RestResult.success("打卡成功",result);
    }
}
