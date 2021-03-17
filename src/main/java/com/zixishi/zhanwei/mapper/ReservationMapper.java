package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.dto.ReservationBySeatDTO;
import com.zixishi.zhanwei.dto.ReservationSaveDTO;
import com.zixishi.zhanwei.model.Reservation;
import org.apache.ibatis.annotations.Mapper;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<Reservation> searchNow(LocalDate today);

    List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime);

    void save(ReservationSaveDTO reservationSaveDTO);

    List<ReservationBySeatDTO> searchBySeatAndDate(Long seatId, LocalDate date);

    Long countBySeatAndDate(Long seatId, LocalDate date);
}
