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
    public List<AreaDto> searchDTO(LocalDate time) {
        List<Area> areas = areaMapper.search();
        List<Reservation> reservations = null;
        if(time.isEqual(LocalDate.now())){
           reservations =  reservationService.searchNow();  //查询当前预约情况
        }else{
          reservations=   reservationService.searchByDate(time);  //根据时间查询预约情况
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
                    if(seat.getId() == seatId) {
                        seatDTO.setUsername(reservation.getUser().getUsername());
                        seatDTO.setStatus(true);
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
}
