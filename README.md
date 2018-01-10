# bubi-mybatis-spring-boot使用说明

标签（空格分隔）： bubi-mybatis-starter datasource jta 

---
 1. [前言](#1)
 2. [概览](#2)
 3. [使用说明](#3)

----------


## <div id="1">前言</div>   ##
为了便于应用开发，和屏蔽访问数据库细节。开发了此组件，主要功能有支持多写数据源，多读数据源，jta事务控制，mybatis封装数据访问功能。本文档侧重于介绍功能和使用功能,实际项目demo参考sample项目。

----------
## <div id="2">概览</div>  ##
主要介绍实现功能的方法

1项目mybatis配置部分使用开源mybatis-spring-boot作为主要参考(版本号2.0.0-SNAPSHOT)，也就是开源的mybatis功能都是支持的。

2多读部分数据源使用org.apache.tomcat.jdbc.pool.DataSource，对应上层采用mybatis原生SqlSession自行封装了线程安全访问，默认行为轮询策略。

3多写部分采用支持jta事务的com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean数据源，对应上层采用mybatis-spring项目的SqlSessionTemplate它主要做了线程安全和封装事务到PlatformTransactionManager供Spring管理，多写可以由@WriteToDb("config db name")指定到相应数据源存储.

4jta事务管理，这部分采用开源的atomikos项目解决，没有定制化改造，不多介绍.


----------
## <div id="3">使用说明</div>  ##
1这里列出配置选项，使用者根据自己的需要自行调整

```ruby
# mybatis 相关配置,可以根据实际情况选
bubi.mybatis.mapper-locations=classpath:mapper/*.xml
bubi.mybatis.type-aliases-package=cn.bubi.mybatis.sample.dao.pojo
bubi.mybatis.config-location=classpath:mybatis-configuration.xml
# read
bubi.datasource.reads[0].username=root
bubi.datasource.reads[0].password=db72
bubi.datasource.reads[0].url=jdbc:mysql://192.168.10.72:3306/test2?useUnicode=true&amp;characterEncoding=UTF-8
bubi.datasource.reads[1].username=root
bubi.datasource.reads[1].password=db72
bubi.datasource.reads[1].url=jdbc:mysql://192.168.10.72:3306/test1?useUnicode=true&amp;characterEncoding=UTF-8
# write dbName在后续指定写数据库用
bubi.datasource.writes[0].dbName=test1
bubi.datasource.writes[0].config.username=root
bubi.datasource.writes[0].config.password=db72
bubi.datasource.writes[0].config.url=jdbc:mysql://192.168.10.72:3306/test1?useUnicode=true&amp;characterEncoding=UTF-8
bubi.datasource.writes[1].dbName=test2
bubi.datasource.writes[1].config.username=root
bubi.datasource.writes[1].config.password=db72
bubi.datasource.writes[1].config.url=jdbc:mysql://192.168.10.72:3306/test2?useUnicode=true&amp;characterEncoding=UTF-8
```

2对于写库部分需要指定写入的库，如果不配置默认采用配置的第一条库，这里列举一个基本的handle
```java
@Component
@WriteToDb("test1")
public class DemoHandle extends SimpleEntityHandler<Demo>{

}
```

> 如果用户需要在自己的Handle调用DataAccess对象进行数据访问，那么需要用户手动调用父类SimpleEntityHandler中的preWriteDb()方法,如果只是增加自己的方法调用父类的数据访问（如super.insert()）,那么不用手动指定数据库.

3事务控制部分完全交给Spring控制，使用者可以自行配置，无论是通过tx标签开启，还是@Transaction注解开启，例子就不写了，都可以的.

> 注册到容器中的事务管理器名称为"jtaTransactionManager"，调用者可以直接从容器中获取


![结束][1]

  [1]: http://chuantu.biz/t6/134/1510216305x1007318759.jpg

