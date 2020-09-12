package com.gbdpcloud.config;

import com.github.pagehelper.PageHelper;
import gbdpcloudcommonbase.gbdpcloudcommonbase.mybatis.plugins.PrepareInterceptor;
import gbdpcloudcommonbase.gbdpcloudcommonbase.mybatis.plugins.ResultInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author 13683
 * @version 1.0
 * @Description
 * @date 2020/6/14 0:56
 */
//@Configuration
//@MapperScan(basePackages = "com.gbdpcloud.mapper.*", sqlSessionFactoryRef = "sqlSessionTemplate")
public class SalverDataSourceConfig {


    @Bean
   // @ConfigurationProperties(prefix = "spring.datasource.salver")
    public DataSource salverDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory salverSqlSessionFactory(@Qualifier("salverDataSource") DataSource master) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(master);
        fb.setTypeAliasesPackage("com.gbdpcloud.entity");

        //配置分页插件，详情请查阅官方文档
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //分页尺寸为0时查询所有纪录不再执行分页
        properties.setProperty("pageSizeZero", "true");
        //页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("reasonable", "true");
        //支持通过 Mapper 接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        pageHelper.setProperties(properties);

        // 自定义数据权限拦截插件
        PrepareInterceptor prepareInterceptor = new PrepareInterceptor();
        ResultInterceptor resultInterceptor = new ResultInterceptor();
        //添加插件
        fb.setPlugins(new Interceptor[]{prepareInterceptor, resultInterceptor, });

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        fb.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        return fb.getObject();
    }


    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("salverDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    public SqlSessionTemplate salverSqlSessionTemplate(@Qualifier("MastersqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("salverSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.gbdpcloud.mapper");

        //配置通用Mapper，详情请查阅官方文档
        Properties properties = new Properties();
        properties.setProperty("mappers", "gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper");
        properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);

        return mapperScannerConfigurer;
    }


}
