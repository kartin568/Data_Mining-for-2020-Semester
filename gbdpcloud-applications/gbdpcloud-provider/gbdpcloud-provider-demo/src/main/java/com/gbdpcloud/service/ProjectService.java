package com.gbdpcloud.service;
import com.gbdpcloud.entity.Project;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface ProjectService extends Service<Project> {

    @ApiOperation(value = "根据项目成员查询项目")
    List<Project> selectByMember(String username);

    @ApiOperation(value = "根据项目组长查询项目")
    List<Project> selectByLeader(String username);

    @ApiOperation(value = "根据项目名获取项目")
    Project selectByName(String name);

    @ApiOperation(value = "根据项目id列表获取项目列表")
    List<Project> selectByIds(String ids);

    @ApiOperation(value = "根据id获取项目")
    Project selectById(String id);

    @ApiOperation(value = "删除项目列表")
    int deleteIds(String ids);
}
