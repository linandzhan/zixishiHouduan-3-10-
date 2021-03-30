package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.dto.LeaderBoardDTO;
import com.zixishi.zhanwei.dto.TongJiIncome;
import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.mapper.ReservationMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Record;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.RecordService;
import com.zixishi.zhanwei.service.UserService;
import com.zixishi.zhanwei.util.RestResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private ReservationMapper reservationMapper;


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

    @Override
    public RestResult leaderBoard(LocalDate start,LocalDate end) {
        List<User> users = userMapper.search(null, null);

        List<LeaderBoardDTO> leaderBoardDTOS = new ArrayList<>();
        int count = 0;
        for (User user : users) {
//            if(count == 10) {
//                return RestResult.success(leaderBoardDTOS);
//            }

            LeaderBoardDTO leaderBoardDTO = new LeaderBoardDTO();
            leaderBoardDTO.setId(user.getId());
            leaderBoardDTO.setUsername(user.getUsername());
           Double amount =  recordMapper.searchPayByUser(user.getId(),start,end);
           leaderBoardDTO.setAmount(amount);
            Long number =   reservationMapper.tongJiyUser(user.getId(),start,end);
            leaderBoardDTO.setNumber(number);
            List<String> areaNames = reservationMapper.searchUserLikeArea(user.getId());
            if(!areaNames.isEmpty()) {
                leaderBoardDTO.setAreaNameLike(areaNames.get(0));
            }else {
                leaderBoardDTO.setAreaNameLike("没有");
            }

            leaderBoardDTOS.add(leaderBoardDTO);

//            count++;
        }

        Collections.sort(leaderBoardDTOS, new Comparator<LeaderBoardDTO>() {
            @Override
            public int compare(LeaderBoardDTO t2, LeaderBoardDTO t1) {
                double v = t1.getAmount() - t2.getAmount();
                return (int) v;
            }
        });

        return RestResult.success(leaderBoardDTOS);
    }
}
