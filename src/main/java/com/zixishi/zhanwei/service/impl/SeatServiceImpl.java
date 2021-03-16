package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.AreaService;
import com.zixishi.zhanwei.service.SeatService;
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


}
