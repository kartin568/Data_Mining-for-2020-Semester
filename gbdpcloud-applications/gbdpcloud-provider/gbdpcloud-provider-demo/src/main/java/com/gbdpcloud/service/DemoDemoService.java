package com.gbdpcloud.service;

import com.gbdpcloud.entity.DemoDemo;
import com.gbdpcloud.mapper.DemoDemoMapper;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-11
 */
public interface DemoDemoService extends Service<DemoDemo> {

    // TODO 这里写自定义的service方法


    DemoDemo querydemo(String userId);

}
