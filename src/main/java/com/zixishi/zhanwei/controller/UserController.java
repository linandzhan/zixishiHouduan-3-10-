package com.zixishi.zhanwei.controller;


import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.config.authorization.annotation.RequiredPermission;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Record;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.RecordService;
import com.zixishi.zhanwei.service.UserService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "用户管理")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RecordService recordService;


    /**
     * 用户查询（search)
     * @param
     */
    @Authorization
    @ApiOperation(value = "查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/user/search")
    //23
    @RolePermission(value = {"超级管理员","管理员"})
    public RestResult search(@RequestBody JSONObject jsonObject) {
        LinkedHashMap pageableMap = (LinkedHashMap) jsonObject.get("pageable");
        Integer page = (Integer) pageableMap.get("page");
        Integer size = (Integer) pageableMap.get("size");
        Pageable pageable = new Pageable();
        pageable.setPage(page);
        pageable.setSize(size);


        LinkedHashMap user = (LinkedHashMap) jsonObject.get("user");
        String phone = null;
        String username = null;
        if(user.size() > 0 ) {
             phone = (String) user.get("phone");
             username = (String) user.get("username");
        }



        return userService.search(phone,username,pageable);
    }


    /**
     * 用户充值（recharge)
     * @param
     */
    @Authorization
    @ApiOperation(value = "用户充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/user/recharge")
    @RolePermission(value = {"超级管理员","管理员","用户"})
    //因为有可能是当前用户自己充值，所以要加多一个CurrentUser
    public RestResult recharge(@RequestBody JSONObject jsonObject,@CurrentUser Account account) {
        Integer id = (Integer) jsonObject.get("id");
        User user = new User();
        if(id != null) {
            user.setId(Long.parseLong(id.toString()));
        }else if(id == null) {
            user.setId(account.getId());
        }
        String rechargeMoney = (String) jsonObject.get("rechargeMoney");


        User attach = userService.attach(account.getId());
        Double v = Double.parseDouble(rechargeMoney);
        user.setBalance(attach.getBalance()+v);

        userMapper.updateBalance(user);
        Record record = new Record();
        record.setUpdateBalance(v);
        record.setUser(user);
        record.setContent("用户充值金额");
        record.setType("收入");
        record.setUpdateTime(LocalDateTime.now());
        recordService.save(record);

        return RestResult.success("已成功充值");
    }



    /**
     * 新增用户（save)
     * @param
     */
    @Authorization
    @ApiOperation(value = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/user/save")
    @RolePermission(value = {"超级管理员","管理员"})
    public RestResult save(@RequestBody Map<String,User> userMap) {
        User user = userMap.get("user");

       userService.save(user);
        return RestResult.success("新增成功");
    }


    /**
     * 查询当前登录用户信息
     * @param
     */
    @Authorization
    @ApiOperation(value = "查询当前登录用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/user/get")
//    @RolePermission(value = {"超级管理员","管理员"})
    public RestResult get(@CurrentUser Account account) {
        Long id = account.getId();
        User user = userService.attach(id);
        return RestResult.success(user);
    }


}
