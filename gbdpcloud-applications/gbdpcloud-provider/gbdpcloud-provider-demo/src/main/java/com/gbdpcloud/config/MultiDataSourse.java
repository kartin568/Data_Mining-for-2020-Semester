package com.gbdpcloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lihaifeng
 * @version 1.0
 * @Description 创建动态数据源
 * @date 2020/6/7 18:31
 */
@Slf4j
public class MultiDataSourse extends AbstractRoutingDataSource {
    /**
     * 通过绑定线程的数据源上下文实现多数据源的动态切换
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourcetype = DataSourceContext.getDataSource();
        logger.info("=========数据源："+ dataSourcetype + "==========");
        return dataSourcetype;

    }
}
