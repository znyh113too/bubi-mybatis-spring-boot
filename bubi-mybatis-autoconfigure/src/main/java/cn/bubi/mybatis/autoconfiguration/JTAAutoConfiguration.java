package cn.bubi.mybatis.autoconfiguration;

import cn.bubi.mybatis.properties.DataSourcesProperties;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.SystemException;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午7:51.
 */
@Configurable
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JTAAutoConfiguration{

    @Bean
    public JtaConfig jtaConfig(DataSourcesProperties dataSourcesProperties){
        if (!dataSourcesProperties.isAtomikosWriteFile()) {
            System.setProperty("com.atomikos.icatch.no_file", "true");
            System.setProperty("com.atomikos.icatch.service", "com.atomikos.icatch.standalone.UserTransactionServiceFactory");
        }
        return null;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager(JtaConfig jtaConfig){
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp userTransactionImp(JtaConfig jtaConfig) throws SystemException{
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);
        return userTransactionImp;
    }

    @Bean
    public PlatformTransactionManager jtaTransactionManager(UserTransactionImp userTransactionImp, UserTransactionManager userTransactionManager, JtaConfig jtaConfig){
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        jtaTransactionManager.setUserTransaction(userTransactionImp);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    public static class JtaConfig{

    }

}
