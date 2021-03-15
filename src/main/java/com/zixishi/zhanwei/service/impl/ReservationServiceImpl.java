package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
@Service
public class ReservationServiceImpl implements ReservationService {
    @Resource
    private ReservationMapper reservationMapper;


    @Override
    public List<Reservation> searchNow() {
        return reservationMapper.searchNow();
    }

    @Override
    public List<Reservation> searchByDate(LocalDate time) {

        return reservationMapper.searchByDate(time);
    }
}
