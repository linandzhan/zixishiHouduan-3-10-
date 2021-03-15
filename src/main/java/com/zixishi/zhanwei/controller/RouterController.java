package com.zixishi.zhanwei.controller;

import com.alibaba.fastjson.JSONObject;
import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.CurrentUser;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.AccountMapper;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.mapper.RouterMapper;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Role;
import com.zixishi.zhanwei.model.Router;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@Api(tags = "路由管理")
public class RouterController {

    @Resource
    private RouterMapper routerMapper;
    @Resource
    private AccountMapper accountMapper;

    /**
     * 角色列表查询
     * @return
     */
    @Authorization
    @ApiOperation(value = "路由查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header"),
    })
    @RolePermission
    @PostMapping("/router/search")
    public RestResult search(@CurrentUser Account account) {

        List<Role> roles = accountMapper.findRoleByAccount(account.getId());
        List<Router> allRouters = new ArrayList<>();
        for (Role role : roles) {
            List<Router> routers = routerMapper.search(role);
            for (Router router : routers) {
                if(!allRouters.contains(router)) {
                    allRouters.add(router);
                }
            }
        }
        Collections.sort(allRouters, new Comparator<Router>() {
            @Override
            public int compare(Router router, Router t1) {
                Long result = router.getId() - t1.getId();
                return Integer.parseInt(result.toString());
            }
        });

//        System.out.println(routers);
        return RestResult.success(allRouters);
    }
}
