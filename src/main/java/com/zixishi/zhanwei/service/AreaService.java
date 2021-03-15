package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.dto.AreaDto;
import com.zixishi.zhanwei.model.Area;

import java.time.LocalDate;
import java.util.List;

public interface AreaService {
    List<Area> search();

    List<AreaDto> searchDTO(LocalDate time);

}
