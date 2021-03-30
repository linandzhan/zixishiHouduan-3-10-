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

    List<Reservation> findFinishByUser(Long id, LocalDate bookDate,LocalDate searchDate);

    Long countCancelByUser(Long id, LocalDate bookDate);

    Long countFinishByUser(Long id, LocalDate bookDate,LocalDate searchDate);

    void cancel(Long id,String reason,Double returnMoney);

    Reservation get(Long id);

    Long searchByStartAndEndAndArea(LocalDate searchStartDate, LocalDate searchEndDate, Long id);

    List<Reservation> searchByUserAndToday(Long id,LocalDate today);

    void updateHaveClock(Long id);

    Long count(String username, String phone);

    List<Reservation> search(String username, String phone);

    void updateStatus(Long id,String status);



    List<Reservation> searchByStatus(String status);

    Long tongJiyUser(Long id, LocalDate firstDay, LocalDate lastDay);

    List<String> searchUserLikeArea(Long id);
}
