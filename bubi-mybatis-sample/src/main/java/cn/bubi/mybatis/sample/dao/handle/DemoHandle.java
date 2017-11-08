package cn.bubi.mybatis.sample.dao.handle;

import cn.bubi.common.dao.WriteToDb;
import cn.bubi.common.handler.SimpleEntityHandler;
import cn.bubi.mybatis.sample.dao.pojo.Demo;
import org.springframework.stereotype.Component;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/7 下午4:41.
 */
@Component
@WriteToDb("writeDb1")
public class DemoHandle extends SimpleEntityHandler<Demo>{

}
