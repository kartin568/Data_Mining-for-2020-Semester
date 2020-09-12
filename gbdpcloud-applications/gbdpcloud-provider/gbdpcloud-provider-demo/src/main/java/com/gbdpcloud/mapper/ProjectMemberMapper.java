package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.ProjectMember;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMemberMapper extends IMapper<ProjectMember> {

    int deleteManyIds(List<String> list);

    List<ProjectMember> selectByProjectID(String id);

    List<ProjectMember> getByMember(String id);
}
