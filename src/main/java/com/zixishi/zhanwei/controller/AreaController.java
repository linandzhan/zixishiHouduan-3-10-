package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.SeatService;
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
import java.util.List;

@RestController
@Api(tags = "区域管理")
public class AreaController {

    @Resource
    private AreaService areaService;

    @ApiOperation(value = "查询区域信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @Authorization
    @PostMapping("/area/search")
    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult search(@RequestBody JSONObject jsonObject) {
        List<AreaDto> areaDtos = areaService.searchDTO(LocalDate.now());
        for (AreaDto areaDto : areaDtos) {
            List<SeatDTO> seatDTO = areaDto.getSeatDTO();
            areaDto.setTotalSeat(seatDTO.size());
        }
        return RestResult.success(areaDtos);
    }


}
