package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    List<Reservation> searchNow();

    List<Reservation> searchByDate(LocalDate time);
}
