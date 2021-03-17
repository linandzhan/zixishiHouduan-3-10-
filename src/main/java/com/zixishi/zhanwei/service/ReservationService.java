package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.util.RestResult;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationService {
    List<Reservation> searchNow();

    List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime);

    RestResult save(LocalDate reservationDate, String areaId, String phone, LocalTime startTime, LocalTime endTime, String seatId);
}
