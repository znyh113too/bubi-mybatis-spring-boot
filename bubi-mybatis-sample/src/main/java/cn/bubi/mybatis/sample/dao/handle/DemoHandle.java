package cn.bubi.mybatis.sample.dao.handle;

import org.springframework.stereotype.Component;

import cn.bubi.common.dao.WriteToDb;
import cn.bubi.common.handler.SimpleEntityHandler;
import cn.bubi.mybatis.sample.dao.pojo.Demo;

/**
 * @author xiezhengchao
 * @since 17/11/7 下午4:41.
 */
@Component
@WriteToDb("test1")
public class DemoHandle extends SimpleEntityHandler<Demo>{

}
