package cn.bubi.common.dao;

/**
 * 为了简化先通过标记的方式路由，后续有精力可以做成在beanRegister时动态设置dataAccess 这样针对不同handle直接指定对应的SqlSession,无论从性能还是侵入性来说都优于目前方式
 * 
 * @author xiezhengchao
 * @since 17/11/8 下午5:05.
 */
public class WriteLocal{

    private static final ThreadLocal<String> WRITE_LOCAL = new ThreadLocal<>();

    public static String getWriteDbName(){
        return WRITE_LOCAL.get();
    }

    public static void setWriteDbName(String name) {
        WRITE_LOCAL.set(name);
    }

}
