package cn.bubi.mybatis.balance.read;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.util.List;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午3:13.
 * 读库配置容器
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
