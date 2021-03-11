package com.zixishi.zhanwei.controller;


import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RequiredPermission;
import com.zixishi.zhanwei.mapper.UserMapper;
import com.zixishi.zhanwei.model.User;
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
    public RestResult recharge(@RequestBody JSONObject jsonObject) {
        Integer id = (Integer) jsonObject.get("id");
        String rechargeMoney = (String) jsonObject.get("rechargeMoney");
        User user = new User();
        user.setId(Long.parseLong(id.toString()));
        User attach = userService.attach(Long.parseLong(id.toString()));
        user.setBalance(attach.getBalance()+Double.parseDouble(rechargeMoney));

        userMapper.updateBalance(user);

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
    public RestResult save(@RequestBody Map<String,User> userMap) {
        User user = userMap.get("user");
       userService.save(user);
        return RestResult.success("新增成功");
    }

}
