package cn.bubi.mybatis.autoconfiguration;

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

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager(){
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp userTransactionImp() throws SystemException{
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);
        return userTransactionImp;
    }

    @Bean
    public PlatformTransactionManager jtaTransactionManager(UserTransactionImp userTransactionImp, UserTransactionManager userTransactionManager){
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        jtaTransactionManager.setUserTransaction(userTransactionImp);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

}
