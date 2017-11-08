package cn.bubi.mybatis.sample.dao.handle;

import cn.bubi.common.dao.WriteToDb;
import cn.bubi.common.handler.SimpleEntityHandler;
import cn.bubi.mybatis.sample.dao.pojo.Demo2;
import org.springframework.stereotype.Component;

/**
 * @author xiezhengchao@bubi.cn
 * @since 17/11/7 下午4:41.
 */
@Component
@WriteToDb("writeDb2")
public class DemoHandle2 extends SimpleEntityHandler<Demo2>{

}
