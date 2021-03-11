package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AreaMapper {

    public Area findOne(Long id);

    List<Area> findAreaListByPackage(Long id);

    List<Area> search();
}
