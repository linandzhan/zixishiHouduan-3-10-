package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.dto.ReservationBySeatDTO;
import com.zixishi.zhanwei.dto.SeatDTO;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {
    List<Seat> search();

    List<SeatDTO> searchByArea(Long parseLong, LocalDate searchTime);

    RestResult searchReservationBySeat(Long seatId, LocalDate date, Pageable pageable);
}
