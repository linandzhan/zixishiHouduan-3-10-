package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.model.Reservation;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@Api(tags = "区域管理")
public class AreaController {

    @Resource
    private AreaService areaService;
    @Resource
    private ReservationService reservationService;

    @ApiOperation(value = "查询区域信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @Authorization
    @PostMapping("/area/search")
    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult search(@RequestBody JSONObject jsonObject) {
        LocalDate searchTime = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        String searchTimeStr = (String) jsonObject.get("searchTime");
        List<String> timeAfter = (List<String>) jsonObject.get("timeAfter");



        if(searchTimeStr != null) {
            searchTime = LocalDate.parse(searchTimeStr);
        }
        if (timeAfter != null) {
            String start = timeAfter.get(0);
            String end = timeAfter.get(1);
            startTime = LocalTime.parse(start);
            endTime = LocalTime.parse(end);
        }

        List<AreaDto> areaDtos = areaService.searchDTO(searchTime,startTime,endTime);
        List<AreaDto> fillareaDtos = areaService.fillareaDtos(areaDtos);


        return RestResult.success(areaDtos);
    }

    @ApiOperation(value = "根据id查询当前区域下的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @Authorization
    @PostMapping("area/getById")
//    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult getById(@RequestBody JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        List<AreaDto> areaDtos = areaService.searchDTO(null, null, null);
        System.out.println(areaDtos);
        List<AreaDto> fillareaDtos = areaService.fillareaDtos(areaDtos);
        for (AreaDto fillareaDto : fillareaDtos) {
            if(fillareaDto.getAreaId() == Long.parseLong(id)) {
                return RestResult.success(fillareaDto);
            }
        }
        return null;
    }


}
