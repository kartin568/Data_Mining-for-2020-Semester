package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UacOfficeService extends Service<UacOffice> {

    /*  @ApiOperation(value = "新增机构")
      public int add(@Valid UacOffice uacOffice,String id);

      @ApiOperation(value = "更新机构信息")
      public int updateInfo(@Valid UacOffice uacOffice,String id);

      @ApiOperation(value = "级联删除机构")
      public int delete(String oid,String uid);
  */
    @ApiOperation(value = "获得机构列表")
    public Map listAsT();


    @ApiOperation(value = "新增机构")
    public int add(@Valid UacOffice uacOffice, String id);

    @ApiOperation(value = "更新机构信息")
    public int updateInfo(@Valid UacOffice uacOffice,String id);

    @ApiOperation(value = "级联删除机构")
    public int delete(String oid,String uid);

    @ApiOperation(value = "获得机构列表")
    public List<UacOffice> listAll();

    @ApiOperation(value = "根据id列表获取机构列表")
    List<UacOffice> getByIds(List<String> ids);

    List<UacOffice> listLeaf();

    int addRoot( UacOffice uacOffice);
}
