package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.dto.TongJiIncome;
import com.zixishi.zhanwei.model.Record;
import com.zixishi.zhanwei.model.User;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {
    void save(Record record);

    List<TongJiIncome> searchIncomeByEveryMonth(LocalDate searchStartDate);
}
