package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.model.Seat;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {
    List<Seat> search();

    List<SeatDTO> searchByArea(Long parseLong, LocalDate searchTime);
}
