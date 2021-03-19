package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.dto.ReservationSaveDTO;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public RestResult save(LocalDate reservationDate, String areaId, String phone, LocalTime startTime, LocalTime endTime, String seatId,Double moeny) {
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
        save.setMoney(moeny);
        reservationMapper.save(save);

        User updateUser = new User();
        updateUser.setBalance(user.getBalance()-moeny);
        updateUser.setId(user.getId());
        userMapper.updateBalance(updateUser);
       return RestResult.success("预约成功");
    }

    @Override
    public RestResult fndByUser(Long userId, LocalDate bookDate, Pageable pageable) {
        List<Reservation> allReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findByUser(userId, bookDate));
//        List<Reservation> endingReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findByUserEnding(userId, bookDate));
//        List<Reservation> cancelReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findByUserCancel(userId, bookDate));
        Long allTotal  = reservationMapper.countByUser(userId, bookDate);
        ListDTO allDTO = new ListDTO();

        allDTO.setTotal(allTotal);
        allDTO.setItems(allReservation);




        return RestResult.success(allDTO);
    }

    @Override
    public RestResult findCancelByUser(Long id, LocalDate bookDate, Pageable pageable) {

        List<Reservation> cancelReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findCancelByUser(id, bookDate));
        Long cancelTotal  = reservationMapper.countCancelByUser(id, bookDate);
        ListDTO allDTO = new ListDTO();

        allDTO.setTotal(cancelTotal);
        allDTO.setItems(cancelReservation);
        return RestResult.success(allDTO);
    }

    @Override
    public RestResult findFinishByUser(Long id, LocalDate bookDate, Pageable pageable) {
        List<Reservation> finishReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findFinishByUser(id, bookDate));
        Long finishTotal  = reservationMapper.countFinishByUser(id, bookDate);
        ListDTO allDTO = new ListDTO();

        allDTO.setTotal(finishTotal);
        allDTO.setItems(finishReservation);
        return RestResult.success(allDTO);
    }

    @Override
    public void cancelReservation(Long id,String reason) {
        reservationMapper.cancel(id,reason);
    }
}
