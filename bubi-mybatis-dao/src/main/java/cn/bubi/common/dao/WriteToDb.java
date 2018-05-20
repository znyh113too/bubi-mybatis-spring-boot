package cn.bubi.common.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 向db写入的标志
 * 
 * @author xiezhengchao
 * @since 17/11/8 下午4:39.
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
