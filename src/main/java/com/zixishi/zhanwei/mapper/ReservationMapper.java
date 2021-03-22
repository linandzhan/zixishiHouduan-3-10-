package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.dto.ReservationBySeatDTO;
import com.zixishi.zhanwei.dto.ReservationSaveDTO;
import com.zixishi.zhanwei.model.Reservation;
import org.apache.ibatis.annotations.Mapper;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<Reservation> searchNow(LocalDate today);

    List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime);

    void save(ReservationSaveDTO reservationSaveDTO);

    List<ReservationBySeatDTO> searchBySeatAndDate(Long seatId, LocalDate date);

    Long countBySeatAndDate(Long seatId, LocalDate date);

    List<Reservation> findByUser(Long userId, LocalDate bookDate);

    Long countByUser(Long userId, LocalDate bookDate);

    List<Reservation> findByUserEnding(Long userId, LocalDate bookDate);

    List<Reservation> findByUserCancel(Long userId, LocalDate bookDate);

    List<Reservation> findCancelByUser(Long id, LocalDate bookDate);

    List<Reservation> findFinishByUser(Long id, LocalDate bookDate);

    Long countCancelByUser(Long id, LocalDate bookDate);

    Long countFinishByUser(Long id, LocalDate bookDate);

    void cancel(Long id,String reason);
}
