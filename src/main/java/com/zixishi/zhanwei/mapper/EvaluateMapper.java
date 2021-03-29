package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Evaluate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EvaluateMapper {
    void save(Evaluate evaluate);

    Evaluate findByClock(Long clockId);
}
