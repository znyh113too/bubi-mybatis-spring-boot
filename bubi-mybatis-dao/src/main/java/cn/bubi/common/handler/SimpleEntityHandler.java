package cn.bubi.common.handler;

import cn.bubi.common.dao.GenericsUtils;
import cn.bubi.common.dao.IDataAccess;
import cn.bubi.common.dao.WriteLocal;
import cn.bubi.common.dao.WriteToDb;
import com.github.pagehelper.PageRowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 顶层Handle
 */
public class SimpleEntityHandler<T extends Serializable> implements IEntityHandler<T>{

    private static final String POSTFIX_INSERT = ".insert";
    private static final String POSTFIX_UPDATE = ".update";
    private static final String POSTFIX_DELETE_PRIAMARYKEY = ".deleteByPrimaryKey";
    private static final String POSTFIX_SELECT = ".select";
    private static final String POSTFIX_SELECTPOJO = ".selectPojo";
    private static final String POSTFIX_LIST = ".list";
    private static final String POSTFIX_GETALL = ".getAll";
    // private static final String POSTFIX_COUNT = ".count";

    private final Logger log = LoggerFactory.getLogger(SimpleEntityHandler.class);
    private final String entityName;
    private final WriteToDb writeToDb;

    public SimpleEntityHandler(){
        entityName = GenericsUtils.getSuperClassGenricType(getClass()).getName();
        writeToDb = this.getClass().getAnnotation(WriteToDb.class);
    }

    @Resource
    private IDataAccess writeDA;

    @Resource
    private IDataAccess readDA;

    // -----------------------------------------------------------------------
    // setters
    // -----------------------------------------------------------------------

    public void setWriteDA(IDataAccess writeDA){
        this.writeDA = writeDA;
    }

    public void setReadDA(IDataAccess readDA){
        this.readDA = readDA;
    }


    private void preWriteDb(){
        if (writeToDb != null)
            WriteLocal.setWriteDbName(writeToDb.value());
    }

    // -----------------------------------------------------------------------
    // IEntityHandler implementations
    // -----------------------------------------------------------------------
    @Override
    public <S> T select(S id){
        if (null == id) {
            throw new IllegalArgumentException();
        }
        try {
            return readDA.get(makeSqlId(POSTFIX_SELECT), id);
        } catch (SQLException e) {
            logError(e, "da.get() failed, id=%s", id.toString());
        }

        return null;
    }

    @Override
    public T selectByPojo(T t){
        if (null == t) {
            throw new IllegalArgumentException();
        }
        try {
            return readDA.get(makeSqlId(POSTFIX_SELECTPOJO), t);
        } catch (SQLException e) {
            logError(e, "da.get() failed, t=%s", t.toString());
        }

        return null;
    }


    @Override
    public void insert(T t){
        if (null == t) {
            throw new IllegalArgumentException();
        }
        try {
            preWriteDb();
            writeDA.create(makeSqlId(POSTFIX_INSERT), t);
        } catch (SQLException e) {
            logError(e, "da.create() failed, t=%s", t.toString());
        }
    }

    @Override
    public int update(T t){
        if (null == t) {
            throw new IllegalArgumentException();
        }
        try {
            preWriteDb();
            return writeDA.update(makeSqlId(POSTFIX_UPDATE), t);
        } catch (SQLException e) {
            logError(e, "da.update() failed, t=%s", t.toString());
        }

        return 0;
    }

    @Override
    public <S> int delete(S id){
        if (null == id) {
            throw new IllegalArgumentException();
        }
        try {
            preWriteDb();
            return writeDA.delete(makeSqlId(POSTFIX_DELETE_PRIAMARYKEY), id);
        } catch (SQLException e) {
            logError(e, "da.delete() failed, t=%s", id.toString());
        }

        return 0;
    }

    @Override
    public <U, S> List<U> queryForList(String statementId, S parameters){
        if (null == statementId || null == parameters) {
            throw new IllegalArgumentException();
        }

        try {
            return readDA.queryList(makeSqlId(statementId), parameters);
        } catch (SQLException e) {
            logError(e, "da.queryList() failed, statementId=%s, parameters=%s", statementId, parameters.toString());
        }

        return new ArrayList<>();
    }

    @Override
    public <U, S> U queryForObject(String statementId, S parameters){
        if (null == statementId || null == parameters) {
            throw new IllegalArgumentException();
        }

        try {
            return readDA.querySingle(makeSqlId(statementId), parameters);
        } catch (SQLException e) {
            logError(e, "da.querySingle() failed, statementId=%s, parameters=%s", statementId, parameters.toString());
        }

        return null;
    }

    @Override
    public <S> int update(String statementId, S parameters){
        if (null == statementId || null == parameters) {
            throw new IllegalArgumentException();
        }
        try {
            preWriteDb();
            return writeDA.update(makeSqlId(statementId), parameters);
        } catch (SQLException e) {
            logError(e, "da.update() failed, statementId=%s, parameters=%s", statementId, parameters.toString());
        }

        return 0;
    }

    @Override
    public <S> int delete(String statementId, S parameters){
        if (null == statementId || null == parameters) {
            throw new IllegalArgumentException();
        }

        try {
            preWriteDb();
            return writeDA.delete(makeSqlId(statementId), parameters);
        } catch (SQLException e) {
            logError(e, "da.delete() failed, statementId=%s, parameters=%s", statementId, parameters.toString());
        }

        return 0;
    }

    @Override
    public <U> List<U> queryForList(String statementId){
        if (null == statementId) {
            throw new IllegalArgumentException();
        }

        try {
            return readDA.queryList(makeSqlId(statementId));
        } catch (SQLException e) {
            logError(e, "da.queryList() failed, statementId=%s", statementId);
        }

        return new ArrayList<>();
    }

    @Override
    public <U> U queryForObject(String statementId){
        if (null == statementId) {
            throw new IllegalArgumentException();
        }

        try {
            return readDA.querySingle(makeSqlId(statementId), null);
        } catch (SQLException e) {
            logError(e, "da.querySingle() failed, statementId=%s", statementId);
        }

        return null;
    }

    @Override
    public int update(String statementId){
        if (null == statementId) {
            throw new IllegalArgumentException();
        }

        try {
            preWriteDb();
            return writeDA.update(makeSqlId(statementId), null);
        } catch (SQLException e) {
            logError(e, "da.update() failed, statementId=%s", statementId);
        }

        return 0;
    }

    @Override
    public int delete(String statementId){
        if (null == statementId) {
            throw new IllegalArgumentException();
        }

        try {
            preWriteDb();
            return writeDA.delete(makeSqlId(statementId), null);
        } catch (SQLException e) {
            logError(e, "da.delete() failed, statementId=%s", statementId);
        }

        return 0;
    }

    @Override
    public <S> List<T> list(List<S> idList){
        if (null == idList) {
            throw new IllegalArgumentException();
        }

        if (0 == idList.size()) {
            return new ArrayList<>();
        }

        try {
            return readDA.queryList(makeSqlId(POSTFIX_LIST), idList);
        } catch (SQLException e) {
            logError(e, "da.queryList() failed, idList.size=%d", idList.size());
        }

        return new ArrayList<>();
    }

    @Override
    public void insertBatch(String statementId, List<T> list){
        if (null == statementId || null == list) {
            throw new IllegalArgumentException();
        }

        if (0 == list.size()) {
            return;
        }

        try {
            preWriteDb();
            writeDA.insertBatch(makeSqlId(statementId), list);
        } catch (SQLException e) {
            logError(e, "da.insertBatch() failed, statementId=%s, list.size=%d", statementId, list.size());
        }
    }

    @Override
    public void updateBatch(String statementId, List<T> list){
        if (null == statementId || null == list) {
            throw new IllegalArgumentException();
        }

        if (0 == list.size()) {
            return;
        }

        try {
            preWriteDb();
            writeDA.updateBatch(makeSqlId(statementId), list);
        } catch (SQLException e) {
            logError(e, "da.updateBatch() failed, statementId=%s, list.size=%d", statementId, list.size());
        }
    }

    @Override
    public <S> void deleteBatch(String statementId, List<S> list){
        if (null == statementId || null == list) {
            throw new IllegalArgumentException();
        }

        if (0 == list.size()) {
            return;
        }

        try {
            preWriteDb();
            writeDA.deleteBatch(makeSqlId(statementId), list);
        } catch (SQLException e) {
            logError(e, "da.deleteBatch() failed, statementId=%s, list.size=%d", statementId, list.size());
        }
    }

    @Override
    public <S> List<S> getAll(){
        try {
            return readDA.all(makeSqlId(POSTFIX_GETALL));
        } catch (SQLException e) {
            logError(e, "da.all() failed, t=%s");
        }

        return new ArrayList<>();
    }

    @Override
    public Page<T> queryListForPage(String statementCntId, String statementListId, Page<T> page){

        if (null == statementCntId || null == statementListId || null == page) {
            throw new IllegalArgumentException();
        }

        return pagingWithOldManner(statementCntId, statementListId, page);
    }

    @Override
    public Page<T> queryListForPage(String statementListId, Page<T> page){
        return pagingWithOldManner(null, statementListId, page);
    }

    private Page<T> pagingWithOldManner(String statementCntId, String statementListId, Page<T> page){
        int totalNums = 0;
        List<T> resultList = null;

        try {
            if (statementCntId == null) {
                // 插件形式查询
                PageRowBounds pageRowBounds = new PageRowBounds(page.getFetchBegin(), page.getFetchNum());
                pageRowBounds.setCount(true);
                resultList = readDA.queryListPage(makeSqlId(statementListId), page, pageRowBounds);
                totalNums = pageRowBounds.getTotal().intValue();
            } else {
                totalNums = readDA.querySingle(makeSqlId(statementCntId), page);
                if (totalNums > 0) {
                    resultList = readDA.queryList(makeSqlId(statementListId), page);
                }
            }

        } catch (SQLException e) {
            logError(e, "paging query failed, statementCntId=%s, statementListId", statementCntId, statementListId);
        }

        return new Page<>(page.getPageBegin(), page.getFetchNum(), totalNums, resultList);
    }

    private static final String MSG_DANOTFOUND = "usingDefaultDA=%s, filterClass=%s, dataAccessListCount=%d, dataAccessListCount=%d," + "defaultdataAccessClass=%s, defaultdataAccessClass=%s, dataObjClass=%s;";

    private void logError(Exception e, String msg, Object... args){
        if (args.length > 0) {
            this.log.error(String.format(msg, args), e);
            return;
        }

        this.log.error(msg, e);
    }

    private String makeSqlId(String statementId){
        if ('.' != statementId.charAt(0)) {
            return String.format("%s.%s", this.entityName, statementId);
        }

        return this.entityName + statementId;
    }

}
