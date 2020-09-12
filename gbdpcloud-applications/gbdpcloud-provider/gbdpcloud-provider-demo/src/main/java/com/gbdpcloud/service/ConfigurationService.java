package com.gbdpcloud.service;

import com.gbdpcloud.entity.Configuration;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface ConfigurationService extends Service<Configuration> {

    @ApiOperation(value = "获取公共配置方案")
    List<Configuration> getCommon();

    @ApiOperation(value = "获取默认方案")
    Configuration getDefault();

    @ApiOperation(value = "根据创建人id返回个人配置方案")
    List<Configuration> getPrivate(String user_id);

    @ApiOperation(value = "根据配置方案名称删除")
     int deleteByName(String name);
}
