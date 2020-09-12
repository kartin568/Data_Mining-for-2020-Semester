package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface UacUserService extends Service<UacUser>{
/*
    @ApiOperation(value = "通过用户名获取用户")
    UacUser getByName(String username);

    @ApiOperation(value = "更新用户信息")
    int updateInfo(UacUser uacUser,String id);


    @ApiOperation(value = "添加用户")
    int addU(UacUser uacUser,String id);


    @ApiOperation(value = "批量添加用户")
    int addList(List<UacUser> list, String id);

    @ApiOperation(value = "获得全部用户列表")
    public List<UacUser> getAll();
    @ApiOperation(value = "获得单个用户的全部信息")
    UacUser getInfoById(String id);
 */


    @ApiOperation(value = "通过用户名获取用户")
    List<UacUser> getByName(String username);

    @ApiOperation(value = "更新用户信息")
    int updateInfo(UacUser uacUser,String id);


    @ApiOperation(value = "添加用户")
    int addU(UacUser uacUser,String id);


    @ApiOperation(value = "批量添加用户")
    int addList(List<UacUser> list,String id);

    @ApiOperation(value = "获得全部用户列表")
    public List<UacUser> getAll();
    @ApiOperation(value = "获得单个用户的全部信息")
    UacUser getInfoById(String id);


}
