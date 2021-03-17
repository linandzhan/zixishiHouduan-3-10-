package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.dto.ReservationSaveDTO;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@Service
public class ReservationServiceImpl implements ReservationService {
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private UserMapper userMapper;



    @Override
    public List<Reservation> searchNow() {
        return reservationMapper.searchNow(LocalDate.now());
    }

    @Override
    public List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime) {

        return reservationMapper.searchByDate(time,afterTime,endTime);
    }

    @Override
    public RestResult save(LocalDate reservationDate, String areaId, String phone, LocalTime startTime, LocalTime endTime, String seatId) {
        User user = userMapper.findByPhone(phone);
        if(user == null) {
         return    RestResult.error("预约失败,请填入已注册的会员电话");
        }
        ReservationSaveDTO save = new ReservationSaveDTO();
        save.setUserId(user.getId());
        save.setAreaId(Long.parseLong(areaId));
        save.setSeatId(Long.parseLong(seatId));
        save.setReservationDate(reservationDate);
        save.setStartTime(startTime);
        save.setEndTime(endTime);
        save.setUsing(true);
        reservationMapper.save(save);
       return RestResult.success("预约成功");
    }
}
