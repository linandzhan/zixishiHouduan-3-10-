package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Record;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RecordMapper {
    List<Record> search(Long userId);

    void save(Record record);

    Long count();

    Double searchIncome(LocalDate firstDay, LocalDate lastDay);

    Double searchCancelMoney(LocalDate firstDay, LocalDate lastDay);



    Double searchPayByUser(Long id,LocalDate firstDay,LocalDate lastDay);
}
