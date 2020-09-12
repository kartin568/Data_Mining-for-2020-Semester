package com.gbdpcloud.service.impl;

import com.gbdpcloud.entity.Configuration;
import com.gbdpcloud.mapper.ConfigurationMapper;
import com.gbdpcloud.service.ConfigurationService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Configuration service层
 *
 * @date 2020-07-29 18:16:16
 */
@Service("configurationService")
@Transactional(readOnly = true)
public class ConfigurationServiceImpl extends BaseService<Configuration> implements ConfigurationService {

    @Resource
    private ConfigurationMapper configurationMapper;

    public List<Configuration> getCommon(){

        List<Configuration> list = configurationMapper.selectCommon();
       return list;
    }

    @Override
    public Configuration getDefault() {
        return configurationMapper.getDefault();
    }

    @Override
    public List<Configuration> getPrivate(String user_id) {
        List<Configuration> list=configurationMapper.selectPrivate(user_id);
        return list;
    }

    public int deleteByName(String name) {
        Configuration configuration=new Configuration();
        configuration.setName(name);
        int i = configurationMapper.delete(configuration);
        return i;
    }
}
