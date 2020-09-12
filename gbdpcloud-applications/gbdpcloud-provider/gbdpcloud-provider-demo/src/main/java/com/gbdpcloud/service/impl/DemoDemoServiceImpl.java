
package com.gbdpcloud.service.impl;

import com.gbdpcloud.config.DataSourceContext;
import com.gbdpcloud.entity.DemoDemo;
import com.gbdpcloud.mapper.DemoDemoMapper;
import com.gbdpcloud.service.DemoDemoService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-11
 */
@Service
@Transactional(readOnly = true)
@EnableTransactionManagement(proxyTargetClass = true)
public class DemoDemoServiceImpl extends BaseService<DemoDemo> implements DemoDemoService {

    @Resource
    private DemoDemoMapper demoDemoMapper;
    @Override
    public DemoDemo querydemo(String id) {
        DataSourceContext.setDataSource("slave");
        DemoDemo demoDemo =  demoDemoMapper.get(id);

        DataSourceContext.getDataSource();
        return demoDemo;
    }

    // TODO 这里写自定义service方法的实现
}
