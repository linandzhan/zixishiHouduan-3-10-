package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.dto.ReservationSaveDTO;
import com.zixishi.zhanwei.dto.TongJiArea;
import com.zixishi.zhanwei.mapper.AreaMapper;
import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.*;
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
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private AreaMapper areaMapper;




    @Override
    public List<Reservation> searchNow() {
        return reservationMapper.searchNow(LocalDate.now());
    }

    @Override
    public List<Reservation> searchByDate(LocalDate time,LocalTime afterTime,LocalTime endTime) {

        return reservationMapper.searchByDate(time,afterTime,endTime);
    }

    //根据区域查询出每个区域下的预约集合。并将
    @Override
    public List<TongJiArea> searchByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        List<String> itemStyles = new ArrayList<>();
        itemStyles.add("#1ab394");
        itemStyles.add("#79d2c0");
        itemStyles.add("#483D8B");
        itemStyles.add("#00BFFF");
        itemStyles.add("#00FA9A");
        itemStyles.add("#556B2F");
        List<Area> areas = areaMapper.search();
        List<TongJiArea> tongJiAreas = new ArrayList<>();
        int i = 0;
        for (Area area : areas) {
                   List<Reservation> reservations =  reservationMapper.searchByStartAndEndAndArea(searchStartDate,searchEndDate,area.getId());
                   Double income = 0D;
                for (Reservation reservation : reservations) {
                    income = income + reservation.getPayAmount();
                }
                TongJiArea tongJiArea = new TongJiArea();
                tongJiArea.setAreaId(area.getId());
                tongJiArea.setValue(income);
                tongJiArea.setName(area.getName());
                tongJiArea.setItemStyle(itemStyles.get(i));
                tongJiAreas.add(tongJiArea);
                i++;
        }
        return tongJiAreas;
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
        Record record = new Record();
        record.setUser(user);
        record.setUpdateTime(LocalDateTime.now());
        record.setType("支出");
        record.setUpdateBalance(moeny);
        record.setContent("预定座位扣费");
        recordMapper.save(record);
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
    public RestResult findFinishByUser(Long id, LocalDate bookDate, Pageable pageable,LocalDate searchDate) {
        List<Reservation> finishReservation =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.findFinishByUser(id, bookDate,searchDate));
        Long finishTotal  = reservationMapper.countFinishByUser(id, bookDate,searchDate);
        ListDTO allDTO = new ListDTO();

        allDTO.setTotal(finishTotal);
        allDTO.setItems(finishReservation);
        return RestResult.success(allDTO);
    }

    @Override
    public void cancelReservation(Long id, String reason, Account account) {
        reservationMapper.cancel(id,reason);
        Record record = new Record();
        User user = new User();
        user.setId(account.getId());
        record.setUser(user);
        record.setContent("取消预约退款到账");
        Reservation reservation = reservationMapper.get(id);
        record.setUpdateBalance(reservation.getPayAmount());
        record.setType("收入");
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.save(record);
//        record.setUpdateBalance();
    }
}
