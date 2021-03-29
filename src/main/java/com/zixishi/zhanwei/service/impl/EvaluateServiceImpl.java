package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.mapper.EvaluateMapper;
import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.model.Evaluate;
import com.zixishi.zhanwei.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class EvaluateServiceImpl implements EvaluateService {
    @Resource
    private EvaluateMapper evaluateMapper;
    @Resource
    private ClockMapper clockMapper;

    @Override
    public void save(Evaluate evaluate) {
        evaluate.setCreateTime(LocalDateTime.now());
        Clock clock = new Clock();
        clock.setId(evaluate.getClock().getId());
        clockMapper.updateHaveComentIsTrue(clock);
        evaluateMapper.save(evaluate);
    }

    @Override
    public Evaluate findByClock(Long clockId) {

        return evaluateMapper.findByClock(clockId);
    }
}
