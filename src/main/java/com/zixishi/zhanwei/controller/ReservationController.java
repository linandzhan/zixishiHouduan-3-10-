package com.zixishi.zhanwei.controller;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.ReservationService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
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
        String moneyStr = (String) jsonObject.get("money");
        String[] arr = moneyStr.split("元");
        Double money = Double.parseDouble(arr[0]);


        LocalTime start = LocalTime.parse(startTimeStr);
        LocalTime end = LocalTime.parse(endTimeStr);

        return reservationService.save(reservationDate,areaId,phone,start,end,seatId,money);
    }




    /**
     * 根据用户查找该用户下的预约历史（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "预定座位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/findAllByUser")
    public RestResult findAllByUser(@RequestBody JSONObject jsonObject, @CurrentUser Account user) throws ParseException {
        System.out.println(jsonObject);
        System.out.println(user);
        String bookDateStr = (String) jsonObject.get("bookDate");
        LocalDate bookDate = null;
        if(bookDateStr != null) {
           bookDate  = LocalDate.parse(bookDateStr);
        }else {
            bookDate = LocalDate.now();
        }



        LinkedHashMap pageableStr = (LinkedHashMap) jsonObject.get("pageable");
        Pageable pageable = new Pageable();
        Integer page = (Integer) pageableStr.get("page");
        Integer size = (Integer) pageableStr.get("size");
        pageable.setPage(page);
        pageable.setSize(size);
        return reservationService.fndByUser(user.getId(),bookDate,pageable);
    }



    /**
     * 根据用户查找该用户下的预约历史（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "预定座位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/findCancelByUser")
    public RestResult findCancelByUser(@RequestBody JSONObject jsonObject, @CurrentUser Account user) throws ParseException {
        String bookDateStr = (String) jsonObject.get("bookDate");
        LocalDate bookDate = null;
        if(bookDateStr != null) {
            bookDate  = LocalDate.parse(bookDateStr);
        }
//        if(bookDate == null) {
//            bookDate = LocalDate.now();
//        }


        LinkedHashMap pageableStr = (LinkedHashMap) jsonObject.get("pageable");
        Pageable pageable = new Pageable();
        Integer page = (Integer) pageableStr.get("page");
        Integer size = (Integer) pageableStr.get("size");
        pageable.setPage(page);
        pageable.setSize(size);
        return reservationService.findCancelByUser(user.getId(),bookDate,pageable);

    }


    /**
     * 根据用户查找该用户下的预约历史（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "根据用户查找该用户下的预约历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/findFinishByUser")
    public RestResult findFinishByUser(@RequestBody JSONObject jsonObject, @CurrentUser Account user) throws ParseException {
        String bookDateStr = (String) jsonObject.get("bookDate");
        LocalDate bookDate = null;
        LocalDate searchDate = null;
        if(bookDateStr != null) {
            searchDate  = LocalDate.parse(bookDateStr);
        }
            bookDate = LocalDate.now();



        LinkedHashMap pageableStr = (LinkedHashMap) jsonObject.get("pageable");
        Pageable pageable = new Pageable();
        Integer page = (Integer) pageableStr.get("page");
        Integer size = (Integer) pageableStr.get("size");
        pageable.setPage(page);
        pageable.setSize(size);
        return reservationService.findFinishByUser(user.getId(),bookDate,pageable,searchDate);
    }





    /**
     * 取消预定（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "取消预定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/cancel")
    public RestResult cancel(@RequestBody JSONObject jsonObject, @CurrentUser Account account) throws ParseException {
        Integer id = (Integer) jsonObject.get("id");
        String reason = (String) jsonObject.get("reason");
        reservationService.cancelReservation(Long.parseLong(id.toString()),reason,account);
        return RestResult.success("取消成功");
    }

    /**
     * 根据用户查询当天预约情况（search)
     * @param
     */
    @ApiOperation(value = "根据用户查询当天预约情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/reservation/getByUserToday")
    public RestResult getByUserToday(@RequestBody JSONObject jsonObject) throws ParseException {
        String username = (String) jsonObject.get("username");
        String password = (String) jsonObject.get("password");


        return reservationService.getByUserToday(username,password);
    }



}
