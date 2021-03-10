package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RequiredPermission;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.ManagerMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.service.ManagerService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@Api(tags = "管理员管理")
public class ManagerController {
    @Resource
    private ManagerMapper managerMapper;
    @Resource
    private ManagerService managerService;
    @Resource
    private AccountService accountService;

    /**
     * 获取单个Manager信息
     * @return
     */
    @Authorization
    @ApiOperation(value = "获取单个Manager信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/get")
    @RequiredPermission("/manager/get")
    public  RestResult get(@RequestBody  JSONObject jsonObject) {
        String str = (String) jsonObject.get("id");
        Long id = Long.parseLong(str);
        Manager manager = managerMapper.get(id);
        return RestResult.success(manager);
    }


    /**
     * 批量启用
     * @param
     */
    @Authorization
    @ApiOperation(value = "批量启用管理员账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/batchEnabled")
    @RequiredPermission("/manager/batchEnabled")
    public RestResult batchEnabled(@RequestBody List<Long> idList) {
        managerMapper.batchEnabled(idList);
        return RestResult.success("启用成功");
    }



    /**
     * 批量禁用
     * @param
     */
    @Authorization
    @ApiOperation(value = "批量禁用管理员账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/batchDisabled")
    @RequiredPermission("/manager/batchDisabled")
    public RestResult batchDisabled(@RequestBody List<Long> idList) {
        managerMapper.batchDisabled(idList);
        return RestResult.success("禁用成功");
    }

    @Authorization
    @ApiOperation(value = "管理员查询")
    @RequiredPermission("/manager/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/search")
    public RestResult search(@RequestBody JSONObject jsonObject) {
        LinkedHashMap manager = (LinkedHashMap) jsonObject.get("manager");
        Manager m = new Manager();
        String username = (String) manager.get("username");
        Boolean enabled = (Boolean) manager.get("enabled");
        m.setUsername(username);
        m.setEnabled(enabled);
        Long roleId = null;
        Integer id = (Integer) jsonObject.get("roleId");
        if(id != null) {
            roleId = Long.parseLong(id.toString());
        }


        LinkedHashMap pageable = (LinkedHashMap) jsonObject.get("pageable");
        Pageable p = new Pageable();
        p.setPage((Integer) pageable.get("page"));
        p.setSize((Integer) pageable.get("size"));

        return managerService.search(roleId,m,p);
    }



    @Authorization
    @ApiOperation(value = "管理员禁用")
    @RequiredPermission("/manager/disable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/disable")
    public RestResult disable(Long id) {
        managerMapper.disable(id);
        return RestResult.success("禁用成功");
    }

    @Authorization
    @ApiOperation(value = "管理员禁用")
    @RequiredPermission("/manager/enable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/enable")
    public RestResult enable(Long id) {
        managerMapper.enable(id);
        return RestResult.success("启用成功");
    }


    @Authorization
    @ApiOperation(value = "新增管理员")
    @RequiredPermission("/manager/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/save")
    public RestResult save(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        LinkedHashMap managerValue = (LinkedHashMap) jsonObject.get("manager");
        Account account = new Account();
        Manager manager = new Manager();
        String username = (String) managerValue.get("username");
        String password = (String) managerValue.get("password");
        String phone = (String)managerValue.get("phone");
        String description = (String)managerValue.get("description");
        String avatar = (String)managerValue.get("avator");
        if(username != null) {
            account.setUsername(username);
            manager.setUsername(username);
        }
        if(password != null) {
            account.setPassword(password);
        }
        if(phone != null) {
            account.setPhone(phone);
            manager.setPhone(phone);
        }
        if(description != null) {
            manager.setDescription(description);
        }
        if(avatar != null) {
            manager.setAvator(avatar);
        }
        accountService.save(account);
        manager.setId(account.getId());
        manager.setCreateTime(LocalDateTime.now());
        manager.setEnabled(true);
        managerService.save(manager);
        return RestResult.success("新增用户成功");
    }



    /**
     * 给当前用户添加角色
     * @return
     */
    @Authorization
    @ApiOperation(value = "给当前用户添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/manager/addRole")
    public RestResult addRole(@RequestBody JSONObject jsonObject) {
        ArrayList<Integer> roleIds = (ArrayList) jsonObject.get("roleIds");
        Integer userId = (Integer) jsonObject.get("id");

        System.out.println(jsonObject);

        return managerService.addRole(roleIds,userId);
    }
}
