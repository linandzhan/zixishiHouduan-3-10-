package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Record;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {
    List<Record> search();

    void save(Record record);

    Long count();

}
