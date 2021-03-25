package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Clock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClockMapper {
    List<Clock> search();

    List<Clock> searchByReservation(Long id);
}
