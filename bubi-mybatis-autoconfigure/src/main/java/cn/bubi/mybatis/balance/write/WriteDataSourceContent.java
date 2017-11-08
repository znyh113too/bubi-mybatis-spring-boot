package cn.bubi.mybatis.balance.write;


import javax.sql.DataSource;
import java.util.List;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午5:37.
 */
public class WriteDataSourceContent{

    private List<DbContent> dbContentList;

    public WriteDataSourceContent(List<DbContent> dbContentList){
        this.dbContentList = dbContentList;
    }

    public List<DbContent> getDbContentList(){
        return dbContentList;
    }

    public void setDbContentList(List<DbContent> dbContentList){
        this.dbContentList = dbContentList;
    }

    public static class DbContent{
        private String name;
        private DataSource dataSource;

        public DbContent(String name, DataSource dataSource){
            this.name = name;
            this.dataSource = dataSource;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public DataSource getDataSource(){
            return dataSource;
        }

        public void setDataSource(DataSource dataSource){
            this.dataSource = dataSource;
        }
    }

}
