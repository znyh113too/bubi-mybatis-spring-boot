package cn.bubi.common.dao;

import java.lang.annotation.*;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/8 下午4:39.
 * 向db写入的标志
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface WriteToDb{

    /**
     * 必须指定配置文件中的db name
     */
    String value() default "";

}
