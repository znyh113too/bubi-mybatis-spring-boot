package cn.bubi.mybatis.balance.read;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/7 下午10:49.
 * 提供线程安全的sqlSession
 */
public class ReadSqlSession implements SqlSession{

    private SqlSession SqlSessionProxy;

    public ReadSqlSession(ReadSqlSessionFactoryContent sqlSessionFactoryContent){
        SqlSessionProxy = (SqlSession) Proxy.newProxyInstance(ReadSqlSession.class.getClassLoader(),
                new Class[] {SqlSession.class}, new SqlSessionInterceptor(sqlSessionFactoryContent));
    }

    @Override
    public <T> T selectOne(String statement){
        return SqlSessionProxy.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter){
        return SqlSessionProxy.selectOne(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement){
        return SqlSessionProxy.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter){
        return SqlSessionProxy.selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds){
        return SqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey){
        return SqlSessionProxy.selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey){
        return SqlSessionProxy.selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds){
        return SqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement){
        return SqlSessionProxy.selectCursor(statement);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter){
        return SqlSessionProxy.selectCursor(statement, parameter);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds){
        return SqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler){
        SqlSessionProxy.select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, ResultHandler handler){
        SqlSessionProxy.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler){
        SqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public int insert(String statement, Object parameter){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public int update(String statement){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public int update(String statement, Object parameter){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public int delete(String statement){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public int delete(String statement, Object parameter){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public void commit(){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public void commit(boolean force){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public void rollback(){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public void rollback(boolean force){
        throw new RuntimeException("read sqlSession can not support write!");
    }

    @Override
    public List<BatchResult> flushStatements(){
        return SqlSessionProxy.flushStatements();
    }

    @Override
    public void close(){
        SqlSessionProxy.close();
    }

    @Override
    public void clearCache(){
        SqlSessionProxy.clearCache();
    }

    @Override
    public Configuration getConfiguration(){
        return SqlSessionProxy.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> type){
        return SqlSessionProxy.getMapper(type);
    }

    @Override
    public Connection getConnection(){
        return SqlSessionProxy.getConnection();
    }

    private class SqlSessionInterceptor implements InvocationHandler{

        private final Logger logger = LoggerFactory.getLogger(SqlSessionInterceptor.class);
        private ReadSqlSessionFactoryContent sqlSessionFactoryContent;

        SqlSessionInterceptor(ReadSqlSessionFactoryContent sqlSessionFactoryContent){
            this.sqlSessionFactoryContent = sqlSessionFactoryContent;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
            // 生成安全的sqlSession
            ReadSqlSessionFactoryContent.InnerContent innerContent = sqlSessionFactoryContent.getCurrentInnerContent();

            logger.info("router read datasource url : " + innerContent.getDataSource().getUrl());

            try (SqlSession sqlSession = innerContent.getSqlSessionFactory().openSession()) {
                return method.invoke(sqlSession, args);
            }
        }
    }
}
