package com.gbdpcloud.service.impl;

import com.gbdpcloud.mapper.UacRoleUserMapper;
import com.gbdpcloud.mapper.UacUserOfficeMapper;
import com.gbdpcloud.service.UacRoleUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UacRoleUserServiceImpl extends BaseService<UacRoleUser> implements UacRoleUserService {

    @Resource
    private UacRoleUserMapper uacRoleUserMapper;

    @Override
    public UacRoleUser getByRidAuid(String rid, String uid) {
        return uacRoleUserMapper.getByRidAuid(rid,uid);
    }

    @Override
    public List<UacRoleUser> getByUserid(String id) {
        return  uacRoleUserMapper.getByUser(id);
    }

    @Override
    public int del(String id) {
        return uacRoleUserMapper.delByID(id);
    }
}
