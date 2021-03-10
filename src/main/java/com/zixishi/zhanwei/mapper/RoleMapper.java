package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.util.Pageable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> search();

    List<Role> searchRole(String roleName, Pageable pageable);

    Long count(String roleName, Pageable pageable);
}
