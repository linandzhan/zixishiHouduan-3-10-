package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Reservation;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<Reservation> searchNow();

    List<Reservation> searchByDate(LocalDate time);
}
