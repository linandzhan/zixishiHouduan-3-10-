package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.service.ClockService;
import com.zixishi.zhanwei.util.RestResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClockServiceImpl implements ClockService {

    @Resource
    private ClockMapper clockMapper;

    @Override
    public RestResult checkSignInOrOut(Long id) {
        String description = null;
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
                description ="已签退打卡";
             return    RestResult.success("返回成功",description);
            }
        }
        return null;
    }
}
