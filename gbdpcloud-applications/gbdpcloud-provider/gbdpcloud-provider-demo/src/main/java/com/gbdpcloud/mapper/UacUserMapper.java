package com.gbdpcloud.mapper;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UacUserMapper extends IMapper<UacUser> {
    List<UacUser> selectByName(String username);
    UacUser get(String id);
    //List<UacUser> getNameById(String id);xw
    //int deleteByPrimaryKey(String id);

    //int insert(UacUser record);

    //int insertSelective(UacUser record);

    //UacUser selectByPrimaryKey(String id);

    //int updateByPrimaryKeySelective(UacUser record);

    //int updateByPrimaryKey(UacUser record);
}