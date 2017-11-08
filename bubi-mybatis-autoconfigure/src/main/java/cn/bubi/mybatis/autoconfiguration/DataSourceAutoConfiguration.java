package cn.bubi.mybatis.autoconfiguration;

import cn.bubi.mybatis.balance.read.ReadDataSourceContent;
import cn.bubi.mybatis.balance.write.WriteDataSourceContent;
import cn.bubi.mybatis.properties.DataSourcesProperties;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午3:15.
 * datasource配置
 */
@Configurable
@EnableConfigurationProperties(DataSourcesProperties.class)
public class DataSourceAutoConfiguration{

    private DataSourcesProperties dataSourcesProperties;

    public DataSourceAutoConfiguration(DataSourcesProperties dataSourcesProperties){
        this.dataSourcesProperties = dataSourcesProperties;
    }

    /**
     * 默认的连接池
     */
    @Bean("writeDataSourceContent")
    public WriteDataSourceContent writeDataSourceContent(){
        List<DataSourcesProperties.WriteContent> writeContentList = dataSourcesProperties.getWrites();

        if (writeContentList == null || writeContentList.isEmpty()) {
            throw new RuntimeException("write datasource config must not empty");
        }

        List<WriteDataSourceContent.DbContent> writeDbList = writeContentList.stream().map(writeContent -> {
            DataSource dataSource = new DataSource(initPoolProperties(writeContent.getConfig()));
            return new WriteDataSourceContent.DbContent(writeContent.getDbName(), dataSource);
        }).collect(Collectors.toList());


        return new WriteDataSourceContent(writeDbList);
    }

    @Bean("readDataSourceContent")
    public ReadDataSourceContent readDataSourceContent(){
        List<DataSourcesProperties.DbConfig> dbConfigs = dataSourcesProperties.getReads();

        if (dbConfigs == null || dbConfigs.isEmpty()) {
            throw new RuntimeException("read datasource config must not empty");
        }

        List<DataSource> dataSourceList = dbConfigs.stream()
                .map(dbConfig -> new DataSource(initPoolProperties(dbConfig)))
                .collect(Collectors.toList());

        return new ReadDataSourceContent(dataSourceList);
    }

    /**
     * 默认配置，可以根据实际需要将配置提到DbConfig中让使用者通过配置管理
     */
    private PoolConfiguration initPoolProperties(DataSourcesProperties.DbConfig dbConfig){
        PoolProperties poolConfiguration = new PoolProperties();
        poolConfiguration.setDriverClassName("com.mysql.jdbc.Driver");
        poolConfiguration.setUsername(dbConfig.getUsername());
        poolConfiguration.setPassword(dbConfig.getPassword());
        poolConfiguration.setUrl(dbConfig.getUrl());
        poolConfiguration.setTestOnBorrow(true);
        poolConfiguration.setTestWhileIdle(true);
        poolConfiguration.setTestOnReturn(false);
        poolConfiguration.setValidationInterval(30000L);
        poolConfiguration.setValidationQuery("SELECT 1");
        poolConfiguration.setTimeBetweenEvictionRunsMillis(30000);
        poolConfiguration.setMaxActive(100);
        poolConfiguration.setInitialSize(10);
        poolConfiguration.setMaxWait(1000);
        poolConfiguration.setMinEvictableIdleTimeMillis(30000);
        poolConfiguration.setMinIdle(10);
        poolConfiguration.setLogAbandoned(false);
        poolConfiguration.setRemoveAbandoned(true);
        poolConfiguration.setRemoveAbandonedTimeout(60);
        poolConfiguration.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        return poolConfiguration;
    }


}
