package com.zixishi.zhanwei.controller;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


@RestController
@Api(tags = "预约管理")
public class ReservationController {
    @Resource
    private ReservationService reservationService;


    /**
     * 用户或管理员预约座位（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "预定座位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/save")
    public RestResult save(@RequestBody JSONObject jsonObject) throws ParseException {
        System.out.println(jsonObject);
        String date = (String) jsonObject.get("date");
        LocalDate reservationDate = LocalDate.parse(date);  //预约哪一天
        String areaId = (String) jsonObject.get("areaId");
        String seatId = (String) jsonObject.get("seatId");
        String phone = (String) jsonObject.get("phone");
        String startTimeStr = (String) jsonObject.get("startTime");
        String endTimeStr = (String) jsonObject.get("endTime");

//        String start = date+" "+startTimeStr;
//        String end = date+" "+endTimeStr;
//
//
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime startT = LocalDateTime.parse(start,df);
//        LocalDateTime endT = LocalDateTime.parse(end,df);
//        LocalDateTime endTime = LocalDateTime.parse(endTimeStr);
        LocalTime start = LocalTime.parse(startTimeStr);
        LocalTime end = LocalTime.parse(endTimeStr);

        return reservationService.save(reservationDate,areaId,phone,start,end,seatId);
    }
}
