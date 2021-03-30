package com.zixishi.zhanwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.dto.*;
import com.zixishi.zhanwei.mapper.*;
import com.zixishi.zhanwei.model.*;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.service.ReservationService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.Duration;
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
    @Resource
    private AccountService accountService;
    @Resource
    private ClockMapper clockMapper;




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
                  //查询成交预约数量
                   Long   number =  reservationMapper.searchByStartAndEndAndArea(searchStartDate,searchEndDate,area.getId());

                    TongJiArea tongJiArea = new TongJiArea();
                    tongJiArea.setAreaId(area.getId());
                    tongJiArea.setValue(number);
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
        save.setHaveClock(false);

        LocalDateTime start = LocalDateTime.of(reservationDate, startTime);
        LocalDateTime end = LocalDateTime.of(reservationDate,endTime);

        if(start.isBefore(LocalDateTime.now()) && end.isAfter(LocalDateTime.now())) {
            //如果是今天的预约
            save.setStatus("进行中");
        }else if (start.isAfter(LocalDateTime.now())) {
            save.setStatus("待开始");
        }else if(end.isBefore(LocalDateTime.now())) {
            save.setStatus("已结束");
        }


        if(user.getBalance()-moeny < 0) {
            return RestResult.error("用户余额不足，请去充钱");
        }
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

        Record record = new Record();

        User user = userMapper.get(account.getId());
        User newUser = new User();
        newUser.setId(user.getId());




        record.setUser(user);
        record.setContent("取消预约退款");  //固定写法
        Reservation reservation = reservationMapper.get(id);
        LocalDate bookDate = reservation.getBookDate();

        LocalDateTime start = LocalDateTime.of(bookDate, reservation.getStartTime());
        Long hour = Duration.between(LocalDateTime.now(), start).toHours();
        Double returnMoney = reservation.getPayAmount();
        if(hour < 5) {
            returnMoney = reservation.getPayAmount()/2;
        }

        record.setUpdateBalance(returnMoney);
        newUser.setBalance(user.getBalance()+returnMoney);
        userMapper.updateBalance(user);


        record.setType("收入");
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.save(record);
        reservationMapper.cancel(id,reason,returnMoney);
//        record.setUpdateBalance();
    }

    @Override
    public RestResult getByUserToday(String username, String password) {
        Account account = accountService.findByUsername(username);
        if(account != null && !account.getPassword().equals(password)) {
            return RestResult.error("密码错误获取不到预约信息");
        }
        List<Reservation> reservations = reservationMapper.searchByUserAndToday(account.getId(),LocalDate.now());
        List<ReservationTodayByUserDTO> todayReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationTodayByUserDTO todayReservation = new ReservationTodayByUserDTO();
            String start = reservation.getStartTime().toString();
            String end = reservation.getEndTime().toString();
            String seatName = reservation.getSeat().getSeatName();
            String description = start+"-"+end+"/"+seatName;
            todayReservation.setLabel(description);
            todayReservation.setValue(reservation.getId());
            todayReservations.add(todayReservation);
        }
        return RestResult.success(todayReservations);
    }

    @Override
    public RestResult search(Pageable pageable, String username, String phone) {
        List<Reservation> reservations = PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->reservationMapper.search(username,phone));
        List<ReservationListDTO> reservationListDTOS = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationListDTO reservationListDTO = new ReservationListDTO();
            reservationListDTO.setBookDate(reservation.getBookDate());
            reservationListDTO.setStartTime(reservation.getStartTime());
            reservationListDTO.setEndTime(reservation.getEndTime());
            reservationListDTO.setSeatName(reservation.getSeat().getSeatName());
            reservationListDTO.setUsername(reservation.getUser().getUsername());
            List<Clock> clocks = clockMapper.searchByReservation(reservation.getId());
            if(!clocks.isEmpty()) {
                reservationListDTO.setClockId(clocks.get(0).getId());
                reservationListDTO.setHaveComment(clocks.get(0).getHaveComment());
            }

            reservationListDTOS.add(reservationListDTO);
        }
       Long total =  reservationMapper.count(username,phone);
        ListDTO listDTO = new ListDTO();
        listDTO.setTotal(total);
        listDTO.setItems(reservationListDTOS);
        return RestResult.success(listDTO);
    }


}
