package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Evaluate;

public interface EvaluateService {
    void save(Evaluate evaluate);

    Evaluate findByClock(Long clockId);
}
