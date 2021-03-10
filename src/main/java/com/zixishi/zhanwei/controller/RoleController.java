package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.service.RoleService;
import com.zixishi.zhanwei.util.Pageable;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "角色管理")
public class RoleController {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleService roleService;

    /**
     * 角色列表查询
     * @return
     */
    @Authorization
    @ApiOperation(value = "角色查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/role/search")
    public RestResult search(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        LinkedHashMap role = (LinkedHashMap) jsonObject.get("role");
        LinkedHashMap pageable = (LinkedHashMap) jsonObject.get("pageable");
        Pageable p = new Pageable();
        p.setPage((Integer) pageable.get("page"));
        p.setSize((Integer) pageable.get("size"));


        List<Role> roles = roleMapper.search();
        return RestResult.success(roles);


    }



    /**
     * 角色列表查询
     * @return
     */
    @Authorization
    @ApiOperation(value = "角色管理页面的角色列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/role/searchRole")
    public RestResult searchRole(@RequestBody JSONObject jsonObject) {

        String roleName = null;
        LinkedHashMap role = (LinkedHashMap) jsonObject.get("role");
        if(role.size() != 0) {
             roleName = (String) role.get("name");
        }
        LinkedHashMap pageable = (LinkedHashMap) jsonObject.get("pageable");
        Pageable p = new Pageable();
        p.setPage((Integer) pageable.get("page"));
        p.setSize((Integer) pageable.get("size"));

//        return RoleService.search()
        return roleService.searchRole(roleName,p);
    }

    /**
     * 角色列表查询
     * @return
     */
    @Authorization
    @ApiOperation(value = "新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/role/save")
    public RestResult save(@RequestBody Map<String,Role> roleMap) {
        System.out.println(roleMap);
        Role role = roleMap.get("role");
        roleMapper.save(role);
        return  RestResult.success("新增角色成功");
    }


    /**
     * 角色列表查询
     * @return
     */
    @Authorization
    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/role/delete")
    public RestResult delete(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        Integer id = (Integer) jsonObject.get("id");
        roleMapper.delete(Long.parseLong(id.toString()));
        return RestResult.success("删除成功");
    }



}
