package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Evaluate;
import com.zixishi.zhanwei.model.User;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.service.EvaluateService;
import com.zixishi.zhanwei.util.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class EvaluateController {
    @Resource
    private EvaluateService evaluateService;
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "用户评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/evaluate/save")
//    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult save(@RequestBody Evaluate evaluate) {
        Account attach = accountService.attach();
        User user = new User();
        user.setId(attach.getId());
        evaluate.setUser(user);
        evaluateService.save(evaluate);
        return RestResult.success("评价成功");
    }

    @ApiOperation(value = "用户评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/evaluate/searchByClock")
    //    @RolePermission(value = {"用户","管理员","超级管理员"})
    public RestResult searchByClock(@RequestBody JSONObject jsonObject) {
        Integer id = (Integer) jsonObject.get("id");
        Evaluate evaluate = evaluateService.findByClock(Long.parseLong(id.toString()));
        return RestResult.success(evaluate);
    }



}
