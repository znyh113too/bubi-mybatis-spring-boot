package cn.bubi.mybatis.balance.write;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;

/**
 * @author xiezhengchao
 * @since 17/11/8 下午5:57.
 */
public class WriteSqlSessionFactoryContent{

    private List<SqlSessionContent> sqlSessionContentList;

    public WriteSqlSessionFactoryContent(List<SqlSessionContent> sqlSessionContentList){
        this.sqlSessionContentList = sqlSessionContentList;
    }

    public List<SqlSessionContent> getSqlSessionContentList(){
        return sqlSessionContentList;
    }

    public void setSqlSessionContentList(List<SqlSessionContent> sqlSessionContentList){
        this.sqlSessionContentList = sqlSessionContentList;
    }

    public static class SqlSessionContent{
        private String name;
        private SqlSession sqlSession;
        private DataSource dataSource;

        public SqlSessionContent(String name, SqlSession sqlSession, DataSource dataSource){
            this.name = name;
            this.sqlSession = sqlSession;
            this.dataSource = dataSource;
        }

        public DataSource getDataSource(){
            return dataSource;
        }

        public void setDataSource(DataSource dataSource){
            this.dataSource = dataSource;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public SqlSession getSqlSession(){
            return sqlSession;
        }

        public void setSqlSession(SqlSession sqlSession){
            this.sqlSession = sqlSession;
        }
    }

}
