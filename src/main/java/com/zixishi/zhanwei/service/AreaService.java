package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.util.RestResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AreaService {
    List<Area> search();

    List<AreaDto> searchDTO(LocalDate time, LocalTime afterTime,LocalTime endTime);

    List<AreaDto> fillareaDtos(List<AreaDto> areaDtos);

    RestResult searchMoneyByAreaAndDate(LocalDate searchStartDate, LocalDate searchEndDate);
}
