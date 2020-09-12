package com.gbdpcloud.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import gbdpcloudcommonbase.gbdpcloudcommonbase.mybatis.plugins.PrepareInterceptor;
import gbdpcloudcommonbase.gbdpcloudcommonbase.mybatis.plugins.ResultInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Mybatis & Mapper & PageHelper 配置
 */
@Configuration
@MapperScan(basePackages = "com.gbdpcloud.mapper.*")
@EnableConfigurationProperties
@EnableTransactionManagement
@Slf4j
public class MybatisConfigurer {

    @Value("${spring.datasource.druid.filters}")
    private String filters;

    @Value("${spring.datasource.druid.initial-size}")
    private Integer initialSize;

    @Value("${spring.datasource.druid.min-idle}")
    private Integer minIdle;

    @Value("${spring.datasource.druid.max-active}")
    private Integer maxActive;

    @Value("${spring.datasource.druid.max-wait}")
    private Integer maxWait;

    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")
    private Long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.druid.test-while-idle}")
    private Boolean testWhileIdle;

    @Value("${spring.datasource.druid.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.test-on-return}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.pool-prepared-statements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.druid.max-pool-prepared-statement-per-connection-size}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    /**
     * 通过Spring JDBC 快速创建 DataSource
     * @return
     */
    @Bean(name = "masterDataSource")
    @Qualifier("masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() throws SQLException {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "salveDataSourse")
    @Qualifier("salveDataSourse")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource salveDataSourse () throws SQLException {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public MultiDataSourse dataSource (@Qualifier("masterDataSource") DataSource master,
                                       @Qualifier("salveDataSourse") DataSource slave){

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", master); //主数据源
        targetDataSources.put("slave", slave); //从数据源

        MultiDataSourse dataSource = new MultiDataSourse();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(master);// 默认的datasource设置为myTestDbDataSource
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource master,
                                               @Qualifier("salveDataSourse") DataSource slave) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource(master, slave));
        fb.setTypeAliasesPackage("com.gbdpcloud.entity");
        //多数据源
//        Interceptor interceptor = new PageInterceptor();
//
//        //配置分页插件，详情请查阅官方文档
//        //PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        //分页尺寸为0时查询所有纪录不再执行分页
//        properties.setProperty("pageSizeZero", "true");
//        //多数据源需要指定dialect
//        properties.setProperty("helperDialect", "mysql");
//        //页码<=0 查询第一页，页码>=总页数查询最后一页
//        properties.setProperty("reasonable", "true");
//        //是否将参数offset作为PageNum使用
//        properties.setProperty("offsetAsPageNum", "true");
//        //是否进行count查询
//        properties.setProperty("rowBoundsWithCount", "true");
//        //支持通过 Mapper 接口参数来传递分页参数
//        properties.setProperty("supportMethodsArguments", "true");
//        //pageHelper.setProperties(properties);
//        interceptor.setProperties(properties);
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
    public DataSourceTransactionManager transactionManager(MultiDataSourse dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }



    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.gbdpcloud.mapper");

        //配置通用Mapper，详情请查阅官方文档
        Properties properties = new Properties();
        properties.setProperty("mappers", "gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper");
        properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);

        return mapperScannerConfigurer;
    }
    @Bean
    @Primary
    public SqlSessionTemplate jckSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}

