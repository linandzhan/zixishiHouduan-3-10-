package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.model.Record;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordMapper recordMapper;


    @Override
    public void save(Record record) {


        recordMapper.save(record);
    }
}
