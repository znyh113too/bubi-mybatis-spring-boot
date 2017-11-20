package cn.bubi.mybatis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/7 下午3:44.
 * 载入读写配置源
 */
@ConfigurationProperties(prefix = DataSourcesProperties.DATA_SOURCES_PREFIX)
public class DataSourcesProperties{

    static final String DATA_SOURCES_PREFIX = "bubi.datasource";

    @NotNull
    private List<WriteContent> writes = new ArrayList<>();

    @NotNull
    private List<DbConfig> reads = new ArrayList<>();


    public List<WriteContent> getWrites(){
        return writes;
    }

    public void setWrites(List<WriteContent> writes){
        this.writes = writes;
    }

    public List<DbConfig> getReads(){
        return reads;
    }

    public void setReads(List<DbConfig> reads){
        this.reads = reads;
    }

    public static class WriteContent{
        private String dbName;
        private DbConfig config;

        public String getDbName(){
            return dbName;
        }

        public void setDbName(String dbName){
            this.dbName = dbName;
        }

        public DbConfig getConfig(){
            return config;
        }

        public void setConfig(DbConfig config){
            this.config = config;
        }
    }

    /**
     * db配置,可以根据需要调整
     */
    public static class DbConfig{
        private String username;
        private String password;
        private String url;
        private int poolSize = 5;// 默认池大小

        public int getPoolSize(){
            return poolSize;
        }

        public void setPoolSize(int poolSize){
            this.poolSize = poolSize;
        }

        public String getUsername(){
            return username;
        }

        public void setUsername(String username){
            this.username = username;
        }

        public String getPassword(){
            return password;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public String getUrl(){
            return url;
        }

        public void setUrl(String url){
            this.url = url;
        }
    }

}
