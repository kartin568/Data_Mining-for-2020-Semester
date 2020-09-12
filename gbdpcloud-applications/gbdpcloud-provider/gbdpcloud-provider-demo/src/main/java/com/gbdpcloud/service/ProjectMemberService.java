package com.gbdpcloud.service;

import com.gbdpcloud.entity.ProjectMember;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface ProjectMemberService extends Service<ProjectMember> {

    @ApiOperation(value = "通过项目成员ID获取用户所在项目列表")
    List<ProjectMember> getByMember(String member_ID);

    @ApiOperation(value = "通过项目ID获取项目成员列表")
    List<ProjectMember> getByProject(String project_ID);

    @ApiOperation(value = "删除项目成员")
    int delete(ProjectMember p);

    @ApiOperation(value = "删除多个成员")
    public int deleteIds(List<String> ids);



}
