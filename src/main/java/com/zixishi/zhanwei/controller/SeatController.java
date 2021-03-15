package com.zixishi.zhanwei.controller;

import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.service.SeatService;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<AreaDto> myResult = areaService.searchDTO(LocalDate.now());

        return RestResult.success(myResult);

    }


}
