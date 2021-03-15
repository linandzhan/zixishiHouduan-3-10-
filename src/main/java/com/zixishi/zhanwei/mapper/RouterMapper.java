package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.model.Router;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouterMapper {
    List<Router> search(Role role);

}
