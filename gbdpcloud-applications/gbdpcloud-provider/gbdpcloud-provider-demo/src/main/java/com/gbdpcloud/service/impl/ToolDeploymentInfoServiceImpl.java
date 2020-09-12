package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.ToolDeploymentInfo;
import com.gbdpcloud.mapper.ToolDeploymentInfoMapper;
import com.gbdpcloud.service.ToolDeploymentInfoService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * ToolDeploymentInfo service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class ToolDeploymentInfoServiceImpl extends BaseService<ToolDeploymentInfo> implements ToolDeploymentInfoService {

    @Resource
    private ToolDeploymentInfoMapper toolDeploymentInfoMapper;

    @Override
    public int deleteByName(String name) {
        ToolDeploymentInfo toolDeploymentInfo=new ToolDeploymentInfo();
        toolDeploymentInfo.setTool(name);
        return toolDeploymentInfoMapper.delete(toolDeploymentInfo);
    }
}
