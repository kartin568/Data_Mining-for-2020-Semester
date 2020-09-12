package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.Project;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper extends IMapper<Project> {

    List<Project> selectIds(List<String> list);

    Project selectById(String id);

    List<Project> getAll();
    //int deleteByPrimaryKey(String id);

   // int insert(Project record);

    //int insertSelective(Project record);

    //Project selectByPrimaryKey(String id);

   // int updateByPrimaryKeySelective(Project record);

    //int updateByPrimaryKey(Project record);

    //通过member查询,获得一个列表
   // List<Project> selectByMember(String member);

    //通过leader查询，获得一个列表
    //List<Project> selectByLeader(String leader);
}