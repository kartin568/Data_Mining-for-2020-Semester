package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.Configuration;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfigurationMapper extends IMapper<Configuration> {

    List<Configuration> selectCommon();

    List<Configuration> selectPrivate(String user_id);

    Configuration getDefault();
}