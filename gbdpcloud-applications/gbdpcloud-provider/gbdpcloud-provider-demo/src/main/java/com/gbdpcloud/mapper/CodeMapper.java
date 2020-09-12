package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.Code;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeMapper extends IMapper<Code> {
    //int deleteByPrimaryKey(String id);

   // int insert(Code record);

    //int insertSelective(Code record);

    //Code selectByPrimaryKey(String id);

    //int updateByPrimaryKeySelective(Code record);

    //int updateByPrimaryKey(Code record);

    List<Code> getByProject(String id);

    List<Code> getByProjectAndVersion(String id,String version);

    List<Code> getByCodeVersion(String id);

    List<Code> selectProjectVserionAndName(@Param("pid")String pid, @Param("version")String version1, @Param("name")String name);
}