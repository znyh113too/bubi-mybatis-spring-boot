package cn.bubi.mybatis.balance.read;

import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * 读库配置容器
 * 
 * @author xiezhengchao
 * @since 17/11/8 下午3:13.
 */
public class ReadDataSourceContent{

    private List<DataSource> dataSourceList;

    public ReadDataSourceContent(List<DataSource> dataSourceList){
        this.dataSourceList = dataSourceList;
    }


    public List<DataSource> getDataSourceList(){
        return dataSourceList;
    }

    public void setDataSourceList(List<DataSource> dataSourceList){
        this.dataSourceList = dataSourceList;
    }
}
