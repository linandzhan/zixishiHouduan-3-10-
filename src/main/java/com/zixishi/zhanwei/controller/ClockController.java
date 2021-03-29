package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.ClockListDTO;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.service.ClockService;
import com.zixishi.zhanwei.util.HttpResult;
import com.zixishi.zhanwei.util.HttpUtil;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    @PostMapping("/clock/searchClcok")
//    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult searchClcok(@RequestBody JSONObject jsonObject) {
        LinkedHashMap pageable1 = (LinkedHashMap) jsonObject.get("pageable");
        Integer page = (Integer) pageable1.get("page");
        Integer size = (Integer) pageable1.get("size");
        Boolean isComment = (Boolean) jsonObject.get("isComment");
        Pageable pageable = new Pageable();
        pageable.setSize(size);
        pageable.setPage(page);
        ListDTO listDTO = new ListDTO();
        List<ClockListDTO> clockListDTOS = new ArrayList<>();
        if(isComment) {
            //已评价
            List<Clock> clockList = PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->clockMapper.searchHaveComment());
            Long total = clockMapper.countHaveComment();
            for (Clock clock : clockList) {
                ClockListDTO clockListDTO = new ClockListDTO();
                clockListDTO.setSigninTime(clock.getSigninTime());
                clockListDTO.setEndTime(clock.getEndTime());
                clockListDTO.setLength(clock.getLength());
                clockListDTO.setSeatName(clock.getSeat().getSeatName());
                clockListDTO.setId(clock.getId());
                clockListDTOS.add(clockListDTO);
            }

            listDTO.setItems(clockListDTOS);
            listDTO.setTotal(total);
        }else {
            //未评价
            List<Clock> clockList = PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->clockMapper.search());
            Long total = clockMapper.count();

            for (Clock clock : clockList) {
                ClockListDTO clockListDTO = new ClockListDTO();
                clockListDTO.setSigninTime(clock.getSigninTime());
                clockListDTO.setEndTime(clock.getEndTime());
                clockListDTO.setLength(clock.getLength());
                clockListDTO.setSeatName(clock.getSeat().getSeatName());
                clockListDTO.setId(clock.getId());
                clockListDTOS.add(clockListDTO);
            }

            listDTO.setItems(clockListDTOS);
            listDTO.setTotal(total);
        }


        return RestResult.success(listDTO);
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
