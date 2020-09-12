package com.gbdpcloud.mapper;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UacUserOfficeMapper extends IMapper<UacUserOffice> {
    //int deleteByPrimaryKey(String id);

    //int insert(UacUserOffice record);

    //int insertSelective(UacUserOffice record);

    //UacUserOffice selectByPrimaryKey(String id);

    //int updateByPrimaryKeySelective(UacUserOffice record);

   // int updateByPrimaryKey(UacUserOffice record);

    //查询user_id获取单个实体
   // UacUserOffice getByUserId(String user_id);

    UacUserOffice getByUidAndOid(@Param("uid")String uid,@Param("oid") String oid);
    int delByID(String id);


    List<UacUserOffice> getByUserId(String id);
}