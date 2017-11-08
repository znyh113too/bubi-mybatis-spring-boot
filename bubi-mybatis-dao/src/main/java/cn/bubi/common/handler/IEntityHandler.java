package cn.bubi.common.handler;

import java.util.List;

public interface IEntityHandler<T>{

    /**
     * 根据主键查询
     */
    <S> T select(S id);

    /**
     * 根据包含主键对象查询
     */
    T selectByPojo(T t);

    /**
     * 插入
     */
    void insert(T t);

    /**
     * 更新
     */
    int update(T t);

    /**
     * 根据主键删除
     */
    <S> int delete(S id);

    /**
     * 根据条件查询列表
     */
    <U, S> List<U> queryForList(String statementId, S parameters);

    /**
     */
    <U, S> U queryForObject(String statementId, S parameters);

    /**
     * 根据条件更新
     */
    <S> int update(String statementId, S parameters);

    /**
     * 根据条件删除
     */
    <S> int delete(String statementId, S parameters);

    /**
     */
    <U> List<U> queryForList(String statementId);

    /**
     */
    <U> U queryForObject(String statementId);

    /**
     * 根据条件更新
     */
    int update(String statementId);

    /**
     * 根据条件删除
     */
    int delete(String statementId);

    /**
     * 根据主键查询
     */
    <S> List<T> list(List<S> idList);

    /**
     * 批量增加
     */
    void insertBatch(String statementId, List<T> list);

    /**
     * 批量更新
     */
    void updateBatch(String statementId, List<T> list);

    /**
     * 批量删除
     */
    <S> void deleteBatch(String statementId, List<S> list);

    /**
     * 查询全部
     */
    <S> List<S> getAll();

    /**
     * 分页查询
     */
    Page<T> queryListForPage(String statementCntId, String statementListId, Page<T> page);

    /**
     * 分页查询,插件形式
     */
    Page<T> queryListForPage(String statementListId, Page<T> page);

}
