package com.gbdpcloud.service;

import com.gbdpcloud.entity.CodeVersion;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface CodeVersionService extends Service<CodeVersion> {

    @ApiOperation("通过项目id获取代码工程列表")
    List<CodeVersion> getByProject(String id);
}
