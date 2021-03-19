package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationService {
    List<Reservation> searchNow();

    List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime);

    RestResult save(LocalDate reservationDate, String areaId, String phone, LocalTime startTime, LocalTime endTime, String seatId,Double money);

    RestResult fndByUser(Long userId, LocalDate bookDate, Pageable pageable);

    RestResult findCancelByUser(Long id, LocalDate bookDate, Pageable pageable);

    RestResult findFinishByUser(Long id, LocalDate bookDate, Pageable pageable);

    void cancelReservation(Long id,String reason);
}
