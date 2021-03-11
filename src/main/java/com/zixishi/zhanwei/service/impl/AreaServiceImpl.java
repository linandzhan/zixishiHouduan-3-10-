package com.zixishi.zhanwei.service.impl;

import com.zixishi.zhanwei.mapper.AreaMapper;
import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.service.AreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Resource
    private AreaMapper areaMapper;

    @Override
    public List<Area> search() {

        return areaMapper.search();
    }
}
