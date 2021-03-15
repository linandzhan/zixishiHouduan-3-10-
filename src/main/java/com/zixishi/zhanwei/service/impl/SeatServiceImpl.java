package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.Seat;
import com.zixishi.zhanwei.service.SeatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {
    @Resource
    private SeatMapper seatMapper;

    @Override
    public List<Seat> search() {
        return seatMapper.search();
    }


}
