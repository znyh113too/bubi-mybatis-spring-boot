package cn.bubi.mybatis.balance.write;

import cn.bubi.common.dao.WriteLocal;
import cn.bubi.mybatis.util.ExceptionUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午5:28.
 * 针对sqlSessionTemplate做路由的指定写数据源操作
 */
public class WriteSqlSession implements SqlSession{
    private SqlSession sqlSessionProxy;

    public WriteSqlSession(WriteSqlSessionFactoryContent writeSqlSessionFactoryContent){
        this.sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(WriteSqlSession.class.getClassLoader(),
                new Class[] {SqlSession.class}, new SqlSessionInterceptor(writeSqlSessionFactoryContent));
    }

    @Override
    public <T> T selectOne(String statement){
        return sqlSessionProxy.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter){
        return sqlSessionProxy.selectOne(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement){
        return sqlSessionProxy.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter){
        return sqlSessionProxy.selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds){
        return sqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey){
        return sqlSessionProxy.selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey){
        return sqlSessionProxy.selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds){
        return sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement){
        return sqlSessionProxy.selectCursor(statement);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter){
        return sqlSessionProxy.selectCursor(statement, parameter);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds){
        return sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler){
        sqlSessionProxy.select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, ResultHandler handler){
        sqlSessionProxy.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler){
        sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement){
        return sqlSessionProxy.insert(statement);
    }

    @Override
    public int insert(String statement, Object parameter){
        return sqlSessionProxy.insert(statement, parameter);
    }

    @Override
    public int update(String statement){
        return sqlSessionProxy.update(statement);
    }

    @Override
    public int update(String statement, Object parameter){
        return sqlSessionProxy.update(statement, parameter);
    }

    @Override
    public int delete(String statement){
        return sqlSessionProxy.delete(statement);
    }

    @Override
    public int delete(String statement, Object parameter){
        return sqlSessionProxy.delete(statement, parameter);
    }

    @Override
    public void commit(){
        sqlSessionProxy.commit();
    }

    @Override
    public void commit(boolean force){
        sqlSessionProxy.commit(force);
    }

    @Override
    public void rollback(){
        sqlSessionProxy.rollback();
    }

    @Override
    public void rollback(boolean force){
        sqlSessionProxy.rollback(force);
    }

    @Override
    public List<BatchResult> flushStatements(){
        return sqlSessionProxy.flushStatements();
    }

    @Override
    public void close(){
        sqlSessionProxy.close();
    }

    @Override
    public void clearCache(){
        sqlSessionProxy.clearCache();
    }

    @Override
    public Configuration getConfiguration(){
        return sqlSessionProxy.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> type){
        return sqlSessionProxy.getMapper(type);
    }

    @Override
    public Connection getConnection(){
        return sqlSessionProxy.getConnection();
    }

    private class SqlSessionInterceptor implements InvocationHandler{

        private final Logger logger = LoggerFactory.getLogger(SqlSessionInterceptor.class);
        private final SqlSessionContent defaultSqlSession;
        private Map<String, SqlSessionContent> nameDataSourceMapping = new HashMap<>();

        SqlSessionInterceptor(WriteSqlSessionFactoryContent writeSqlSessionFactoryContent){
            writeSqlSessionFactoryContent.getSqlSessionContentList()
                    .forEach(sqlSessionContent -> nameDataSourceMapping.put(sqlSessionContent.getName(), new SqlSessionContent(sqlSessionContent.getDataSource(), sqlSessionContent.getSqlSession())));
            defaultSqlSession = nameDataSourceMapping.values().iterator().next();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

            SqlSessionContent useSqlSession;
            if (useDefaultSqlSession()) {
                useSqlSession = defaultSqlSession;
            } else {
                useSqlSession = nameDataSourceMapping.get(WriteLocal.getWriteDbName());
            }

            logger.info("router write datasource url : " + useSqlSession.getDataSource().getUrl());

            try {
                return method.invoke(useSqlSession.getSqlSession(), args);
            } catch (Exception e) {
                throw ExceptionUtil.unwrapThrowable(e);
            }
        }

        private boolean useDefaultSqlSession(){
            String name = WriteLocal.getWriteDbName();
            return nameDataSourceMapping.size() == 1 || StringUtils.isEmpty(name) || nameDataSourceMapping.get(name) == null;
        }
    }

    private class SqlSessionContent{
        private DataSource dataSource;
        private SqlSession sqlSession;

        SqlSessionContent(DataSource dataSource, SqlSession sqlSession){
            this.dataSource = dataSource;
            this.sqlSession = sqlSession;
        }

        DataSource getDataSource(){
            return dataSource;
        }

        SqlSession getSqlSession(){
            return sqlSession;
        }
    }

}
