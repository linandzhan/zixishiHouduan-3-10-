package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.dto.ReservationBySeatDTO;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.SeatService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {
    @Resource
    private SeatMapper seatMapper;
    @Resource
    private AreaService areaService;
    @Resource
    private ReservationMapper reservationMapper;

    @Override
    public List<Seat> search() {
        return seatMapper.search();
    }

    @Override
    public List<SeatDTO> searchByArea(Long id, LocalDate searchTime) {
        List<AreaDto> areaDtos = areaService.searchDTO(searchTime, LocalTime.now(),LocalTime.now());
        List<SeatDTO> seatDTOS = null;
        for (AreaDto areaDto : areaDtos) {
            if(areaDto.getAreaId() == id) {
                seatDTOS = areaDto.getSeatDTO();
            }
        }
        return seatDTOS;
    }

    @Override
    public RestResult searchReservationBySeat(Long seatId, LocalDate date, Pageable pageable) {
        List<ReservationBySeatDTO> list =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.searchBySeatAndDate(seatId, date));
        Long total = reservationMapper.countBySeatAndDate(seatId,date);
        ListDTO listDTO = new ListDTO();
        listDTO.setItems(list);
        listDTO.setTotal(total);
        return RestResult.success(listDTO);
    }


}
