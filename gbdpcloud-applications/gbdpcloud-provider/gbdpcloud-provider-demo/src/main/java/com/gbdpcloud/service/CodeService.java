package com.gbdpcloud.service;

import com.gbdpcloud.entity.Code;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;


public interface CodeService extends Service<Code> {

    @ApiOperation("通过项目id获取代码列表")
    List<Code> getByProject(String id);

    @ApiOperation("通过项目id和版本号获取代码列表")
    List<Code> getByProjectAndVersion(String id,String version);

    @ApiOperation("通过代码工程id获取代码列表")
    List<Code> getByCodeVersion(String id);

    Code getByProjectVserionAndName(@Valid String pid, @Valid String version1, @Valid String name);

}
