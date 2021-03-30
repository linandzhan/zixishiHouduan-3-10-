package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Package;
import com.zixishi.zhanwei.util.RestResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PackageMapper {

    public Package findOne(Long id);


    List<Package> search();

    Package get(Long id);
}
