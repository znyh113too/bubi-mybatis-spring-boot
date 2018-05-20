package cn.bubi.mybatis.autoconfiguration;

import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.bubi.common.dao.IDataAccess;
import cn.bubi.common.dao.MyBatisDataAccessRead;
import cn.bubi.common.dao.MyBatisDataAccessWrite;

/**
 * 配置顶层dao所需bean
 * 
 * @author xiezhengchao
 * @since 17/11/7 下午3:12.
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class BubiMybatisAutoConfiguration{

    /**
     * 写支持
     */
    @Bean("writeDA")
    public IDataAccess writeDA(SqlSession writeSqlSession){
        return new MyBatisDataAccessWrite(writeSqlSession);
    }

    /**
     * 读支持
     */
    @Bean("readDA")
    public IDataAccess readDA(SqlSession readSqlSession){
        return new MyBatisDataAccessRead(readSqlSession);
    }

}
