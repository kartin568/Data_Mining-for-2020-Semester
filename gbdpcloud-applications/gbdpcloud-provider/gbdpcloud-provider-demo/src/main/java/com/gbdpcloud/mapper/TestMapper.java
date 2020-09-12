package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.Test;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapper extends IMapper<Test> {
   // int deleteByPrimaryKey(String testId);

    //int insert(Test record);

    //int insertSelective(Test record);

   // Test selectByPrimaryKey(String testId);

    //int updateByPrimaryKeySelective(Test record);

    //int updateByPrimaryKey(Test record);
    List<Test> getByProject(String id);

    Test get(String id);
}