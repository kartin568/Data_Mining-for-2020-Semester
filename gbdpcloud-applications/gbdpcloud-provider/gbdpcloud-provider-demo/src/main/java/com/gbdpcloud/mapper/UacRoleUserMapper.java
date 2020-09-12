package com.gbdpcloud.mapper;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UacRoleUserMapper extends IMapper<UacRoleUser> {
    //int deleteByPrimaryKey(String id);

    //int insert(UacRoleUser record);

    //int insertSelective(UacRoleUser record);

    //UacRoleUser selectByPrimaryKey(String id);

    //int updateByPrimaryKeySelective(UacRoleUser record);

    //int updateByPrimaryKey(UacRoleUser record);

    UacRoleUser getByRidAuid(@Param("rid")String rid,@Param("uid") String uid);

    int delByID(String id);

    List<UacRoleUser> getByUser(String id);
}