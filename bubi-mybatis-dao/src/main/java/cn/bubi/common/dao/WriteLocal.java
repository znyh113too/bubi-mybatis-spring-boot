package cn.bubi.common.dao;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午5:05.
 */
public class WriteLocal{

    private static final ThreadLocal<String> WRITE_LOCAL = new ThreadLocal<>();

    public static void setWriteDbName(String name){
        WRITE_LOCAL.set(name);
    }

    public static String getWriteDbName(){
        return WRITE_LOCAL.get();
    }

}
