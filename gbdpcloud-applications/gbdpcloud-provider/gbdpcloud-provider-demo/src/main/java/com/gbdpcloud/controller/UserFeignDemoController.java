package com.gbdpcloud.controller;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.MenuService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * user Feign 远程操作类
 *
 * @author kongweichang
 */
@Api(description = "user Feign 远程操作类")
@RestController
@Slf4j
@RequestMapping("/feign/uac")
public class UserFeignDemoController extends BaseController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户")
    @GetMapping("/user/{id}")
    public Result getUser(@PathVariable String id) {
        return userService.get(id);
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping("/menu")
    public Result listMenu() {
        return menuService.listAll();
    }
}
