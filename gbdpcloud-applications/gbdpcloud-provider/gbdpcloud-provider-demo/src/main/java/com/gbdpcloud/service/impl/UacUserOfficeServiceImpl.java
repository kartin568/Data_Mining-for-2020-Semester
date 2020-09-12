package com.gbdpcloud.service.impl;

import com.gbdpcloud.mapper.UacUserOfficeMapper;
import com.gbdpcloud.service.UacUserOfficeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UacUserOfficeServiceImpl extends BaseService<UacUserOffice> implements UacUserOfficeService {

    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;

    public List<UacUserOffice> getByUserId(String id){
        return uacUserOfficeMapper.getByUserId(id);
    }

    @Override
    public UacUserOffice getByUserAndOffice(String uid, String oid) {
        return uacUserOfficeMapper.getByUidAndOid(uid,oid);
    }

    public int del(String id){
       return  uacUserOfficeMapper.delByID(id);
    }
}

