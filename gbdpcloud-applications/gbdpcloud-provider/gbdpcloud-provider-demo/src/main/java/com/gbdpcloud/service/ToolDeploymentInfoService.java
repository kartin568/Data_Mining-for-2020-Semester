package com.gbdpcloud.service;

import com.gbdpcloud.entity.ToolDeploymentInfo;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

public interface ToolDeploymentInfoService extends Service<ToolDeploymentInfo> {

    @ApiOperation("根据服务器名删除")
    int deleteByName(String name);
}
