package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Seat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeatMapper {
    public List<Seat> findSeatByArea(Long id);

    List<Seat> search();
}
