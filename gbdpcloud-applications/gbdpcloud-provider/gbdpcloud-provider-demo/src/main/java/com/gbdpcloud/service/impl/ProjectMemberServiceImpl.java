package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.ProjectMember;
import com.gbdpcloud.mapper.ProjectMemberMapper;
import com.gbdpcloud.service.ProjectMemberService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectMemberServiceImpl extends BaseService<ProjectMember> implements ProjectMemberService {
    @Resource
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public List<ProjectMember> getByMember(String member_ID) {

        return projectMemberMapper.getByMember(member_ID);
    }

    @Override
    public List<ProjectMember> getByProject(String project_ID) {

        return projectMemberMapper.selectByProjectID(project_ID);
    }

    @Override
    public int delete(ProjectMember p) {
        return projectMemberMapper.delete(p);
    }

    public int deleteIds(List<String> ids) {

        return projectMemberMapper.deleteManyIds(ids);

    }
}
