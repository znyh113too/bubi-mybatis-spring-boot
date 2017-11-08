package cn.bubi.common.dao;

import com.github.pagehelper.PageRowBounds;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface IDataAccess{

    <T> void create(String statementId, T t) throws SQLException;

    <T extends Serializable> T get(String statementId, T t) throws SQLException;

    <T extends Serializable, S> T get(String statementId, S id) throws SQLException;

    <S> int delete(String statementId, S parameters) throws SQLException;

    <T extends Serializable> int update(String statementId, T t) throws SQLException;

    <S> int update(String statementId, S parameters) throws SQLException;

    <T extends Serializable> int insertBatch(String statementId, List<T> list) throws SQLException;

    <T extends Serializable> int updateBatch(String statementId, List<T> list) throws SQLException;

    <S> int deleteBatch(String statementId, List<S> list) throws SQLException;

    // --------------------------------------------------------------------------
    // Query operations
    // --------------------------------------------------------------------------
    <U> List<U> all(String statementId) throws SQLException;

    <U, S> U querySingle(String statementId, S parameters) throws SQLException;

    <U, S> List<U> queryList(String statementId, List<S> idList) throws SQLException;

    <U, S> List<U> queryList(String statementId, S parameters) throws SQLException;

    <U> List<U> queryList(String statementId) throws SQLException;

    /**
     * 分页插件支持
     */
    <U, S> List<U> queryListPage(String statementId, S parameters, PageRowBounds pageRowBounds) throws SQLException;

}
