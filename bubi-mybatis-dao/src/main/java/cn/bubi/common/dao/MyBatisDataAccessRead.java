package cn.bubi.common.dao;

import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午4:49.
 */
public class MyBatisDataAccessRead implements IDataAccess{

    private SqlSession sqlSession;

    public MyBatisDataAccessRead(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    @Override
    public <T> void create(String statementId, T t) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <S> int delete(String statementId, S parameters) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <T extends Serializable> int update(String statementId, T t) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <S> int update(String statementId, S parameters) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <T extends Serializable> int insertBatch(String statementId, List<T> list) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <T extends Serializable> int updateBatch(String statementId, List<T> list) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <S> int deleteBatch(String statementId, List<S> list) throws SQLException{
        throw new RuntimeException("read dataAccess can not support write method.");
    }

    @Override
    public <T extends Serializable> T get(String statementId, T t) throws SQLException{
        return sqlSession.selectOne(statementId, t);
    }

    @Override
    public <T extends Serializable, S> T get(String statementId, S id) throws SQLException{
        return sqlSession.selectOne(statementId, id);
    }

    @Override
    public <U> List<U> all(String statementId) throws SQLException{
        return sqlSession.selectList(statementId);
    }

    @Override
    public <U, S> U querySingle(String statementId, S parameters) throws SQLException{
        return sqlSession.selectOne(statementId, parameters);
    }

    @Override
    public <U, S> List<U> queryList(String statementId, List<S> idList) throws SQLException{
        return sqlSession.selectList(statementId, idList);
    }

    @Override
    public <U, S> List<U> queryList(String statementId, S parameters) throws SQLException{
        return sqlSession.selectList(statementId, parameters);
    }

    @Override
    public <U, S> List<U> queryListPage(String statementId, S parameters, PageRowBounds pageRowBounds) throws SQLException{
        return sqlSession.selectList(statementId, parameters, pageRowBounds);
    }

    @Override
    public <U> List<U> queryList(String statementId) throws SQLException{
        return sqlSession.selectList(statementId);
    }

}
