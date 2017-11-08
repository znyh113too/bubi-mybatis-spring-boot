package cn.bubi.mybatis.balance.read;

import cn.bubi.mybatis.balance.read.queue.CircularBlockingQueue;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.util.List;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午2:40.
 * 提供对SqlSessionFactory的轮询处理
 */
public class ReadSqlSessionFactoryContent{

    private CircularBlockingQueue<InnerContent> circularBlockingQueue = new CircularBlockingQueue<>();
    private InnerContent defaultInnerContent;

    public ReadSqlSessionFactoryContent(List<InnerContent> innerContents){
        if (innerContents.size() == 1) {
            defaultInnerContent = innerContents.get(0);
        } else {
            innerContents.forEach(innerContent -> circularBlockingQueue.add(innerContent));
        }
    }

    InnerContent getCurrentInnerContent(){
        if (defaultInnerContent != null) {
            return defaultInnerContent;
        }
        return circularBlockingQueue.next();
    }


    public static class InnerContent{
        private DataSource dataSource;
        private SqlSessionFactory sqlSessionFactory;

        public InnerContent(DataSource dataSource, SqlSessionFactory sqlSessionFactory){
            this.dataSource = dataSource;
            this.sqlSessionFactory = sqlSessionFactory;
        }

        DataSource getDataSource(){
            return dataSource;
        }

        SqlSessionFactory getSqlSessionFactory(){
            return sqlSessionFactory;
        }
    }

}
