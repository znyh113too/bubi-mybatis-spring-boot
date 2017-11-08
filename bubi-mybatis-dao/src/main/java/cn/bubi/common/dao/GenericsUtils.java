package cn.bubi.common.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {  
    private static Logger log = LoggerFactory.getLogger(GenericsUtils.class);
  
    private GenericsUtils() {  
    }  
  
     
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {  
        return getSuperClassGenricType(clazz, 0);  
    }  
  
   
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {  
        Type genType = clazz.getGenericSuperclass();  
       
        if (!(genType instanceof ParameterizedType)) {  
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");  
            return Object.class;  
        }  
  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "  
                    + params.length);  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");  
            return Object.class;  
        }  
       
        return (Class<?>) params[index];  
    }  
  
  
}  
