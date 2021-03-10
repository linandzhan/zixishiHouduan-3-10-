package com.zixishi.zhanwei.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
@Mapper
public interface PermissionMapper {
    Boolean findByPath(String path);

    Set<String> findByUser(long id);

    List<String> searchPath();


    void save(String path);
}
