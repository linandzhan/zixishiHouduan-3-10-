package com.zixishi.zhanwei.service;

import com.zixishi.zhanwei.model.Clock;
import com.zixishi.zhanwei.util.RestResult;

import java.util.List;

public interface ClockService {
    RestResult checkSignInOrOut(Long id);

    String sign(Long id, String status);


}
