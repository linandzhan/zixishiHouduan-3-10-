package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.config.authorization.annotation.RequiredPermission;
import com.zixishi.zhanwei.config.authorization.token.TokenManager;
import com.zixishi.zhanwei.config.authorization.token.TokenModel;
import com.zixishi.zhanwei.dto.LoginDTO;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.AreaMapper;
import com.zixishi.zhanwei.mapper.ManagerMapper;
import com.zixishi.zhanwei.mapper.PackageMapper;
import com.zixishi.zhanwei.model.*;
import com.zixishi.zhanwei.model.Package;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.util.Constants;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.connector.Request;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "账号管理")
public class AccountController {


    @Resource
    private AccountService accountService;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private PackageMapper packageMapper;
    @Resource
    private ManagerMapper managerMapper;


    @ApiOperation(value = "用户登录接口")
    @ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "登录名"), @ApiImplicitParam(name = "password", value = "密码", required = true)})
    @PostMapping("/account/login")
    public RestResult login( String username,String password) {
        Assert.notNull(username, "username can not be empty");
        Assert.notNull(password, "password can not be empty");
        Account account = accountService.findByUsername(username);
        if(account != null && !account.getPassword().equals(password)) {
            return RestResult.error("密码错误或者账号未注册");
        }
        Manager manager = managerMapper.get(account.getId());
        if(manager.getEnabled() == false) {
            return RestResult.error("该账户已被禁用，请联系管理员");
        }
        TokenModel model = tokenManager.createToken(account.getId());
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setId(model.getId());
        loginDTO.setToken(model.getToken());
        loginDTO.setUsername(username);
        return RestResult.success(loginDTO);
    }



    @Authorization
    @ApiOperation(value = "退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/account/logout")
    @RequiredPermission("/account/logout")
    public RestResult logout(@CurrentUser Account user) {
        tokenManager.deleteToken(user.getId());
        return RestResult.success("退出成功");
    }


    @PostMapping("/account/getAccount")
    @Authorization
    public RestResult getAccount(@CurrentUser Account user) {
        return RestResult.success(user);
    }



    @Authorization
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RequiredPermission("/account/update")
    @PostMapping("/account/update")
    public RestResult update(@CurrentUser Account user) {
        System.out.println(user);
//        tokenManager.deleteToken(user.getId());
//        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
        return RestResult.success("修改成功");
    }



    @Authorization
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RequiredPermission("/account/delete")
    @PostMapping("/account/delete")
    public RestResult delete(Account user) {
//        tokenManager.deleteToken(user.getId());
//        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
        return RestResult.success("删除成功");
    }

    @Authorization
    @ApiOperation(value = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/account/save")
    @RequiredPermission("/account/save")
    public RestResult save(Account user) {
//        tokenManager.deleteToken(user.getId());
//        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
        return RestResult.success("新增成功");
    }


    @Authorization
    @ApiOperation(value = "搜索用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RequiredPermission("/account/search")
    @PostMapping("/account/search")
    public RestResult search(Account user) {
//        tokenManager.deleteToken(user.getId());
//        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
        return RestResult.success("搜索成功");
    }


    @PostMapping("/account/testMapper")
     public RestResult testAreaMapper() {
         Area area = areaMapper.findOne(1l);
         System.out.println(area);
        Package aPackage = packageMapper.findOne(1l);
        List<Area> areas = areaMapper.findAreaListByPackage(aPackage.getId());
        aPackage.setAreaList(areas);
        return RestResult.success(aPackage);
     }


    @Authorization
    @ApiOperation(value = "根据指定用户查找当前用户的角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/account/findRoleByAccount")
     public RestResult findRoleByAccount(@RequestBody JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        Account account = new Account();
        account.setId(Long.parseLong(id));
        return accountService.findRoleByAccount(account);
     }


}
