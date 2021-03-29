package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.dto.ListDTO;
import com.zixishi.zhanwei.mapper.RecordMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Manager;
import com.zixishi.zhanwei.model.Record;
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
import java.util.List;

@RestController
@Api(tags = "打卡记录管理")
public class RecordController {
    @Resource
    private RecordMapper recordMapper;

    /**
     * 搜索当前用户扣费记录
     * @return
     */
    @Authorization
    @ApiOperation(value = "搜索当前用户扣费记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/record/search")
//    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult search(@RequestBody Pageable pageable, @CurrentUser Account account) {
        System.out.println(pageable);
        List<Record> list =  PageHelper.startPage(pageable.getPage(),pageable.getSize()).doSelectPage(()->recordMapper.search(account.getId()));
        Long total = recordMapper.count();
        ListDTO listDTO = new ListDTO();
        listDTO.setTotal(total);
        listDTO.setItems(list);
        return RestResult.success(listDTO);
    }


    /**
     * 测试参数
     * @return
     */
    @Authorization
    @ApiOperation(value = "测试参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/record/param")
//    @RolePermission(value = {"管理员","超级管理员","用户"})
    public RestResult param(@RequestBody JSONObject size) {
        System.out.println(size);
        return  null;
    }
}
