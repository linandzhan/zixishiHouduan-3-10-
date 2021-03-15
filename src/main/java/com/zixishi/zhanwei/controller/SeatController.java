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

//        List<Seat> seats = seatService.search();

       List<Reservation> reservations =  reservationService.searchNow();  //查询当前预约情况
//        for (Reservation reservation : reservations) {
//            Long seatId = reservation.getSeat().getId();
//        }
        List<AreaDto> myResult  = new ArrayList<>();
        for(Area area:areas) {
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaId(area.getId());
            areaDto.setAreaName(area.getName());

            List<SeatDTO> seatDTOS = new ArrayList<>();
            List<Seat> seats = area.getSeatList();



                for (Seat seat : seats) {
                    SeatDTO seatDTO = new SeatDTO();
                    seatDTO.setSeatId(seat.getId());
                    seatDTO.setSeatName(seat.getSeatName());
                    for (Reservation reservation : reservations) {
                        Long seatId = reservation.getSeat().getId();
                    if(seat.getId() == seatId) {
                        seatDTO.setUsername(reservation.getUser().getUsername());
                        seatDTO.setStatus(true);
                    }
                }
                    seatDTOS.add(seatDTO);
            }

            areaDto.setSeatDTO(seatDTOS);

            myResult.add(areaDto);
        }

        return RestResult.success(myResult);

    }


}
