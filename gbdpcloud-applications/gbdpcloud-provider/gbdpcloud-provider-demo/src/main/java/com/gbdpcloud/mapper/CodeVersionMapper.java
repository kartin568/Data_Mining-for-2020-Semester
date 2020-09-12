package com.gbdpcloud.mapper;

import com.gbdpcloud.entity.CodeVersion;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeVersionMapper extends IMapper<CodeVersion> {

    List<CodeVersion> selectByProject(String id);
}
