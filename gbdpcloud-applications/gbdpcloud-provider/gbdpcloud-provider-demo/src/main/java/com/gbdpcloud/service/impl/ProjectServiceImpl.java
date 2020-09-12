package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.Project;
import com.gbdpcloud.mapper.ProjectMapper;
import com.gbdpcloud.service.ProjectService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Project service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl extends BaseService<Project> implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    public List<Project> selectByMember(String member){

        Project project=new Project();
        project.setMember(member);
        return projectMapper.select(project);
      //  return projectMapper.selectByMember(username);
    }

    public List<Project> selectByLeader(String leader){

        Project project=new Project();
        project.setMember(leader);
        return projectMapper.select(project);
      //  return projectMapper.selectByLeader(username);
    }

    @Override
    public Project selectByName(String name) {
        Project project=new Project();
        project.setName(name);
        return projectMapper.selectOne(project);

    }

    public List<Project> selectByIds(String ids) {


        return projectMapper.selectIds(Arrays.asList(ids.split(",")));

    }

    public Project selectById(String id) {

        return projectMapper.get(id);

    }

    public int deleteIds(String ids) {

        return projectMapper.deleteByIds(ids);

    }
}
