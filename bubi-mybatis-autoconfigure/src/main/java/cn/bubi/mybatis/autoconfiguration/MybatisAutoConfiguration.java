package cn.bubi.mybatis.autoconfiguration;

import cn.bubi.mybatis.balance.read.ReadDataSourceContent;
import cn.bubi.mybatis.balance.read.ReadSqlSession;
import cn.bubi.mybatis.balance.read.ReadSqlSessionFactoryContent;
import cn.bubi.mybatis.balance.write.WriteDataSourceContent;
import cn.bubi.mybatis.balance.write.WriteSqlSession;
import cn.bubi.mybatis.balance.write.WriteSqlSessionFactoryContent;
import cn.bubi.mybatis.properties.MybatisProperties;
import cn.bubi.mybatis.support.ConfigurationCustomizer;
import cn.bubi.mybatis.support.SpringBootVFS;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 引用开源项目，针对自身配置定制
 * 这里只做了读写分离部分的bean对象创建
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration{

    private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);

    private final MybatisProperties properties;

    private Interceptor[] interceptors;

    private final ResourceLoader resourceLoader;

    private final DatabaseIdProvider databaseIdProvider;

    private final List<ConfigurationCustomizer> configurationCustomizers;

    private Interceptor pageHelp = pageHelp();

    public MybatisAutoConfiguration(MybatisProperties properties,
                                    ObjectProvider<Interceptor[]> interceptorsProvider,
                                    ResourceLoader resourceLoader,
                                    ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                    ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider){
        this.properties = properties;
        this.interceptors = interceptorsProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    }

    @PostConstruct
    public void checkConfigFileExists(){
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource
                    + " (please add config file or check your Mybatis configuration)");
        }
    }

    private Interceptor pageHelp(){
        // 分页配置
        Interceptor pageHelp = new PageInterceptor();

        Properties pageHelpProperties = new Properties();
        // 设置count查询
        pageHelpProperties.setProperty("rowBoundsWithCount", "true");
        pageHelpProperties.setProperty("offsetAsPageNum", "true");

        pageHelp.setProperties(pageHelpProperties);
        return pageHelp;
    }

    private SqlSessionFactory sqlSessionFactory(javax.sql.DataSource dataSource, Interceptor pageHelp){
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        // 分页插件
        if (pageHelp != null) {
            if (interceptors == null) {
                interceptors = new Interceptor[] {pageHelp};
            } else {
                Set<Interceptor> interceptorList = new HashSet<>(Arrays.asList(interceptors));
                interceptorList.add(pageHelp);
                interceptors = interceptorList.toArray(new Interceptor[interceptorList.size()]);
            }
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }
        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            factory.setMapperLocations(this.properties.resolveMapperLocations());
        }

        try {
            return factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 启用spring代理过的sqlSession做写数据源，事务托管到spring管理
     */
    @Bean
    public SqlSession writeSqlSession(@Qualifier("writeDataSourceContent") WriteDataSourceContent writeDataSourceContent) throws Exception{
        final ExecutorType executorType = this.properties.getExecutorType();

        List<WriteSqlSessionFactoryContent.SqlSessionContent> sqlSessionContentList = writeDataSourceContent
                .getDbContentList().stream().map(dbContent -> {
                    SqlSessionFactory sqlSessionFactory = sqlSessionFactory(dbContent.getDataSource(), null);
                    SqlSession sqlSession;
                    if (executorType != null) {
                        sqlSession = new SqlSessionTemplate(sqlSessionFactory, executorType);
                    } else {
                        sqlSession = new SqlSessionTemplate(sqlSessionFactory);
                    }
                    return new WriteSqlSessionFactoryContent.SqlSessionContent(dbContent.getName(), sqlSession, dbContent.getDataSource());
                }).collect(Collectors.toList());


        return new WriteSqlSession(new WriteSqlSessionFactoryContent(sqlSessionContentList));
    }

    /**
     * 启用自己的sqlSession作读取操作
     */
    @Bean
    public SqlSession readSqlSession(@Qualifier("readDataSourceContent") ReadDataSourceContent readDataSourceContent) throws Exception{

        List<ReadSqlSessionFactoryContent.InnerContent> sqlSessionFactoryList = readDataSourceContent.getDataSourceList().stream()
                .map(dataSourceRead -> new ReadSqlSessionFactoryContent.InnerContent(dataSourceRead, sqlSessionFactory(dataSourceRead, pageHelp)))
                .collect(Collectors.toList());

        ReadSqlSessionFactoryContent sqlSessionFactoryContent = new ReadSqlSessionFactoryContent(sqlSessionFactoryList);

        return new ReadSqlSession(sqlSessionFactoryContent);
    }

    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware{

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry){

            logger.debug("Searching for mappers annotated with @Mapper");

            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }

                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                if (logger.isDebugEnabled()) {
                    for (String pkg : packages) {
                        logger.debug("Using auto-configuration base package '{}'", pkg);
                    }
                }

                scanner.setAnnotationClass(Mapper.class);
                scanner.registerFilters();
                scanner.doScan(StringUtils.toStringArray(packages));
            } catch (IllegalStateException ex) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.", ex);
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader){
            this.resourceLoader = resourceLoader;
        }
    }

    @org.springframework.context.annotation.Configuration
    @Import({AutoConfiguredMapperScannerRegistrar.class})
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration{

        @PostConstruct
        public void afterPropertiesSet(){
            logger.debug("No {} found.", MapperFactoryBean.class.getName());
        }
    }

}
