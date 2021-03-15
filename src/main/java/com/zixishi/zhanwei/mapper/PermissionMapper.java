package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Permission;
import com.zixishi.zhanwei.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
@Mapper
public interface PermissionMapper {
    Boolean findByPath(String path);

    Set<String> findByUser(long id);

    List<Permission> searchPath();


    void save(String path);

    void bind(Role role, Permission permission);

    List<Permission> searchByRole(Role role);

    Permission attach(String path);
}
