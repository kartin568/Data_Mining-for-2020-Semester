package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface UacUserOfficeService extends Service<UacUserOffice> {


    @ApiOperation(value = "通过用户Id获取")
    List<UacUserOffice> getByUserId(String id);

    UacUserOffice getByUserAndOffice(String uid,String oid);

    public int del(String id);
}
