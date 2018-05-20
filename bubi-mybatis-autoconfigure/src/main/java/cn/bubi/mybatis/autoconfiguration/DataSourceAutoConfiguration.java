package cn.bubi.mybatis.autoconfiguration;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.atomikos.jdbc.AtomikosSQLException;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;

import cn.bubi.mybatis.balance.read.ReadDataSourceContent;
import cn.bubi.mybatis.balance.write.WriteDataSourceContent;
import cn.bubi.mybatis.properties.DataSourcesProperties;

/**
 * datasource配置
 * 
 * @author xiezhengchao
 * @since 17/11/8 下午3:15.
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
    public WriteDataSourceContent writeDataSourceContent(AtomikosNonXADataSourceBeanContent content){
        return new WriteDataSourceContent(content.getBeans());
    }

    @Bean("readDataSourceContent")
    public ReadDataSourceContent readDataSourceContent(){
        List<DataSourcesProperties.DbConfig> dbConfigs = dataSourcesProperties.getReads();

        if (dbConfigs == null || dbConfigs.isEmpty()) {
            throw new RuntimeException("read datasource config must not empty");
        }

        List<DataSource> dataSourceList = dbConfigs.stream()
                .map(dbConfig -> new DataSource(initDefaultPoolProperties(dbConfig)))
                .collect(Collectors.toList());

        return new ReadDataSourceContent(dataSourceList);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public AtomikosNonXADataSourceBeanContent atomikosNonXADataSourceBeans(){

        List<DataSourcesProperties.WriteContent> writeContentList = dataSourcesProperties.getWrites();

        if (writeContentList == null || writeContentList.isEmpty()) {
            throw new RuntimeException("write datasource config must not empty");
        }

        List<WriteDataSourceContent.DbContent> writeDbList = writeContentList.stream().map(writeContent -> {
            javax.sql.DataSource dataSource = initJtaPoolProperties(writeContent.getConfig());
            return new WriteDataSourceContent.DbContent(writeContent.getDbName(), dataSource);
        }).collect(Collectors.toList());

        return new AtomikosNonXADataSourceBeanContent(writeDbList);
    }

    /**
     * 写默认配置，可以根据实际需要将配置提到DbConfig中让使用者通过配置管理
     */
    private AtomikosNonXADataSourceBean initJtaPoolProperties(DataSourcesProperties.DbConfig dbConfig){

        AtomikosNonXADataSourceBean atomikosNonXADataSourceBean = new AtomikosNonXADataSourceBean();
        atomikosNonXADataSourceBean.setUniqueResourceName(dbConfig.getUrl() + Instant.now());
        atomikosNonXADataSourceBean.setDriverClassName("com.mysql.jdbc.Driver");
        atomikosNonXADataSourceBean.setUrl(dbConfig.getUrl());
        atomikosNonXADataSourceBean.setUser(dbConfig.getUsername());
        atomikosNonXADataSourceBean.setPassword(dbConfig.getPassword());
        atomikosNonXADataSourceBean.setTestQuery("SELECT 1");
        atomikosNonXADataSourceBean.setPoolSize(dbConfig.getPoolSize());
        atomikosNonXADataSourceBean.setMaxPoolSize(200);
        atomikosNonXADataSourceBean.setMaxLifetime(1000);

        return atomikosNonXADataSourceBean;
    }

    /**
     * 读默认配置，可以根据实际需要将配置提到DbConfig中让使用者通过配置管理
     */
    private PoolProperties initDefaultPoolProperties(DataSourcesProperties.DbConfig dbConfig){

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
        poolConfiguration.setInitialSize(dbConfig.getPoolSize());
        poolConfiguration.setMaxWait(1000);
        poolConfiguration.setMinEvictableIdleTimeMillis(30000);
        poolConfiguration.setLogAbandoned(false);
        poolConfiguration.setRemoveAbandoned(true);
        poolConfiguration.setRemoveAbandonedTimeout(60);
        poolConfiguration.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        return poolConfiguration;
    }

    public static class AtomikosNonXADataSourceBeanContent {
        private List<WriteDataSourceContent.DbContent> beans;

        public AtomikosNonXADataSourceBeanContent(List<WriteDataSourceContent.DbContent> beans) {
            this.beans = beans;
        }

        public List<WriteDataSourceContent.DbContent> getBeans() {
            return beans;
        }

        public void init() throws AtomikosSQLException {
            for (WriteDataSourceContent.DbContent atomikosNonXADataSourceBean : beans) {
                ((AtomikosNonXADataSourceBean) atomikosNonXADataSourceBean.getDataSource()).init();
            }
        }

        public void close() {
            for (WriteDataSourceContent.DbContent atomikosNonXADataSourceBean : beans) {
                ((AtomikosNonXADataSourceBean) atomikosNonXADataSourceBean.getDataSource()).close();
            }
        }
    }

}
