package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.model.Reservation;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.ClockService;
import com.zixishi.zhanwei.util.RestResult;
import net.sf.jsqlparser.expression.operators.relational.Between;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ClockServiceImpl implements ClockService {

    @Resource
    private ClockMapper clockMapper;
    @Resource
    private ReservationMapper reservationMapper;
    @Resource
    private SeatMapper seatMapper;

    @Override
    public RestResult checkSignInOrOut(Long id) {
        String description = null;
        Reservation reservation = reservationMapper.get(id);

        if(reservation.getStartTime().isAfter(LocalTime.now())) {
            description = "该自习还未开始";
            return RestResult.success("返回成功",description);
        }
        if( reservation.getEndTime().isBefore(LocalTime.now())) {
            description = "该自习已结束";
            return RestResult.success("返回成功",description);
        }
        List<Clock> clocks =  clockMapper.searchByReservation(id);
        if(clocks.isEmpty()) {
            description = "签到打卡";
            return RestResult.success("返回成功",description);
        }else if(clocks.size() == 1) {
            Clock clock = clocks.get(0);
            LocalDateTime signoutTime = clock.getEndTime();
            if(signoutTime == null) {
                description = "签退打卡";
               return RestResult.success("返回成功",description);
            }else if (signoutTime!=null) {
                description ="该自习已结束";
             return    RestResult.success("返回成功",description);
            }
        }
        return null;
    }

    @Override
    public String sign(Long id, String status) {
        Reservation reservation = reservationMapper.get(id);
        Clock clock = new Clock();
        clock.setSeat(reservation.getSeat());
        clock.setUser(reservation.getUser());
        clock.setReservation(reservation);
        clock.setHaveComment(false);

        if("签到打卡".equals(status)) {
            clock.setSigninTime(LocalDateTime.now());
            clockMapper.save(clock);
            Seat seat = new Seat();
            seat.setId(reservation.getSeat().getId());
            seat.setStatus(true);  //表示有人在自习了
            seatMapper.updateStaus(seat);
            reservationMapper.updateHaveClock(id);
            return "签到成功";
        }else if("签退打卡".equals(status)) {
            List<Clock> clocks = clockMapper.searchByReservation(id);
            Clock oldClock = clocks.get(0);
            Clock newClock = new Clock();


            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime signinTime = oldClock.getSigninTime();
            Duration between = Duration.between(signinTime, endTime);
            Long minutes = between.toMinutes();
            newClock.setLength(minutes);
            newClock.setId(oldClock.getId());
            newClock.setEndTime(endTime);

            Seat seat = new Seat();
            clockMapper.update(newClock);
            seat.setId(reservation.getSeat().getId());
            seat.setStatus(false);  //表示有人签退了
            seatMapper.updateStaus(seat);
            reservationMapper.updateStatus(id,"已结束");
            return "签退成功";
        }
        return null;
    }
}
