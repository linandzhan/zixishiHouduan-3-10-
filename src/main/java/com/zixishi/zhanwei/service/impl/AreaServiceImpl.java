package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.mapper.AreaMapper;
import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private ReservationService reservationService;
    @Override
    public List<Area> search() {

        return areaMapper.search();
    }

    @Override
    public List<AreaDto> searchDTO(LocalDate time, LocalTime afterTime,LocalTime endTime) {
        List<Area> areas = areaMapper.search();
        List<Reservation> reservations = null;
//        if(time.isEqual(LocalDate.now()) && afterTime.equals(LocalTime.now())){
//           reservations =  reservationService.searchNow();  //查询当前预约情况
//        }else if()){
//          reservations=   reservationService.searchByDate(time);  //根据时间查询预约情况
//        }
        if(time == null && afterTime==null){
            reservations =  reservationService.searchNow();
        }else if (time!=null && afterTime == null) {
            afterTime = LocalTime.parse("00:00:00");
            reservations = reservationService.searchByDate(time,afterTime,endTime);
        }else if (time ==null && afterTime!=null) {
            time = LocalDate.now();
            reservations = reservationService.searchByDate(time, afterTime,endTime);
        }else {
            reservations = reservationService.searchByDate(time, afterTime,endTime);
        }

        List<AreaDto> myResult  = new ArrayList<>();
        for(Area area:areas) {
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaId(area.getId());
            areaDto.setAreaName(area.getName());
            areaDto.setAmount(area.getAmount());
            List<SeatDTO> seatDTOS = new ArrayList<>();
            List<Seat> seats = area.getSeatList();
            for (Seat seat : seats) {
                SeatDTO seatDTO = new SeatDTO();
                seatDTO.setSeatId(seat.getId());
                seatDTO.setSeatName(seat.getSeatName());
                if(reservations.size() == 0) {
                    seatDTO.setStatus(false);
                }
                for (Reservation reservation : reservations) {
                    Long seatId = reservation.getSeat().getId();
                    if(seat.getId() == seatId && reservation.getHaveUsing()==true) {
                        seatDTO.setUsername(reservation.getUser().getUsername());
                        seatDTO.setStatus(true);
                        break;  //当前时间。该座位已经有一个预约匹配上了。
                    }else {
                        seatDTO.setStatus(false);
                    }
                }
                seatDTOS.add(seatDTO);
            }

            areaDto.setSeatDTO(seatDTOS);

            myResult.add(areaDto);
        }
        return myResult;
    }

    @Override
    public List<AreaDto> fillareaDtos(List<AreaDto> areaDtos) {
        for (AreaDto areaDto : areaDtos) {
            List<SeatDTO> seatDTOs = areaDto.getSeatDTO();
            areaDto.setTotalSeat(seatDTOs.size());
            int countUse = 0;
            for (SeatDTO seatDTO : seatDTOs) {
                if(seatDTO.getStatus()) {
                    //该座位现在有人在用着
                    countUse++;
                }
            }
            areaDto.setRemainingSeat(areaDto.getTotalSeat()-countUse);
        }
        return areaDtos;
    }
}
