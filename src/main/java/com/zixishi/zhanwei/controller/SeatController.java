package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.*;
import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.service.SeatService;
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
import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@Api(tags = "座位管理")
public class SeatController {

    @Resource
    private AreaService areaService;
    @Resource
    private SeatService seatService;
    @Resource
    private ReservationService reservationService;
    @Resource
    private SeatMapper seatMapper;

    /**
     * 实时查询座位状态（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "实时查询座位状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/seat/search")
    public RestResult search() {
        List<Area> areas = areaService.search();
        List<AreaDto> myResult = areaService.searchDTO(LocalDate.now(), LocalTime.now(),LocalTime.now());

        return RestResult.success(myResult);
    }

    /**
     * 实时查询座位状态（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "实时查询座位状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
//    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/seat/searchOptions")
    public RestResult searchOptions(@RequestBody JSONObject jsonObject) {
        LocalDate reservationDate  = null;
        LocalTime start = null;
        LocalTime end = null;




        String startTimeStr = (String) jsonObject.get("startTime");
        String endTimeStr = (String) jsonObject.get("endTime");
        String date = (String) jsonObject.get("date");
        if(startTimeStr != null) {
            start = LocalTime.parse(startTimeStr);
        }
        if(endTimeStr != null) {
            end = LocalTime.parse(endTimeStr);
        }
        if(date != null) {
            reservationDate = LocalDate.parse(date);
        }


        List<AreaDto> myResult = areaService.searchDTO(reservationDate,start,end);
        List<AreaOptionDTO> options = new ArrayList<>();
        for (AreaDto areaDto : myResult) {
            AreaOptionDTO areaOptionDTO = new AreaOptionDTO();
            areaOptionDTO.setLabel(areaDto.getAreaName());
            areaOptionDTO.setValue(areaDto.getAreaId().toString());

            List<SeatDTO> seatDtos = areaDto.getSeatDTO();
            List<SeatOptionDTO> seatOptionDTOS = new ArrayList<>();
            for (SeatDTO seatDto : seatDtos) {
                SeatOptionDTO seatOptionDTO = new SeatOptionDTO();
                seatOptionDTO.setLabel(seatDto.getSeatName());
                seatOptionDTO.setValue(seatDto.getSeatId().toString());
                seatOptionDTO.setDisabled(seatDto.getStatus());
                seatOptionDTOS.add(seatOptionDTO);
            }
            areaOptionDTO.setChildren(seatOptionDTOS);
            options.add(areaOptionDTO);
        }


        return RestResult.success(options);
    }



    /**
     * 根据区域查询该区域下的座位（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "根据区域查询该区域下的座位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RolePermission(value = {"超级管理员","管理员","用户"})
    @PostMapping("/seat/searchByArea")
    public RestResult searchByArea(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);

        LocalDate searchTime = null;
        String  searchTimeStr = (String) jsonObject.get("searchTime");
        String id = (String) jsonObject.get("id");
        if(searchTimeStr == null) {
            searchTime = LocalDate.now();
        }else  {
            searchTime = LocalDate.parse(searchTimeStr);
        }


        List<SeatDTO> seatDTOS = seatService.searchByArea(Long.parseLong(id),searchTime);

        return RestResult.success(seatDTOS);
    }


    /**
     * 根据座位查询预约情况（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "根据座位查询预约情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RolePermission(value = {"超级管理员","管理员"})
    @PostMapping("/seat/searchReversation")
    public RestResult searchReversation(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        LocalDate date = null;
        String dateStr = (String) jsonObject.get("date");
        Integer id = (Integer) jsonObject.get("id");
        LinkedHashMap pageableLink = (LinkedHashMap) jsonObject.get("pageable");
        Pageable pageable = new Pageable();
        Integer page = (Integer) pageableLink.get("page");
        Integer size = (Integer) pageableLink.get("size");
        pageable.setPage(page);
        pageable.setSize(size);
        if(dateStr!=null) {
            date = LocalDate.parse(dateStr);
        }else {
            date  = LocalDate.now();
        }



        return seatService.searchReservationBySeat(Long.parseLong(id.toString()),date,pageable);
    }



    /**
     * 根据座位查询预约情况（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "根据座位查询预约情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/seat/get")
    public RestResult get(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        String id = (String) jsonObject.get("id");
       Seat seat =  seatMapper.get(Long.parseLong(id));
        return RestResult.success(seat);
    }







}
