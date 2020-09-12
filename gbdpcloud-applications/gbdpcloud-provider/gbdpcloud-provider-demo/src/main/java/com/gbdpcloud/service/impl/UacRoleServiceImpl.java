package com.gbdpcloud.service.impl;

import com.gbdpcloud.mapper.UacRoleMapper;
import com.gbdpcloud.service.UacRoleService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UacRoleServiceImpl extends BaseService<UacRole> implements UacRoleService {

    @Resource
    private UacRoleMapper uacRoleMapper;

    @Override
    public List<UacRole> listAll() {
        return uacRoleMapper.selectAll();
    }
}
