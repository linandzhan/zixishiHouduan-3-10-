package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.dto.TongJiIncome;
import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.model.Record;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordMapper recordMapper;


    @Override
    public void save(Record record) {


        recordMapper.save(record);
    }

    @Override
    public List<TongJiIncome> searchIncomeByEveryMonth(LocalDate searchStartDate) {
        //获取当年的第一天
        LocalDate fistDay = searchStartDate.with(TemporalAdjusters.firstDayOfYear());
        List<TongJiIncome> incomes = new ArrayList<>();
        int year = fistDay.getYear();
        while(fistDay.getYear() == year) {  //统计当年内
            System.out.println(fistDay.getMonthValue());
            String monthName = fistDay.getMonthValue()+"月";
            LocalDate lastDay = fistDay.with(TemporalAdjusters.lastDayOfMonth());   //获取这一个月的最后一天
            Double totalIncomeEveryMonth = recordMapper.searchIncome(fistDay,lastDay);
            Double cancelMoneyEveryMonth = recordMapper.searchCancelMoney(fistDay,lastDay);   //因为有取消预约退款金额出现，要剪掉
            Double income = totalIncomeEveryMonth-cancelMoneyEveryMonth;
            TongJiIncome tongJiIncome = new TongJiIncome();
            tongJiIncome.setIncome(income);
            tongJiIncome.setMonth(monthName);
            incomes.add(tongJiIncome);

            fistDay = fistDay.plusMonths(1);
        }

        return incomes;
    }
}
