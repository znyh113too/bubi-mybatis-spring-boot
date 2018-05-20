package cn.bubi.mybatis.sample.service;

import cn.bubi.mybatis.sample.dao.pojo.Demo;
import cn.bubi.mybatis.sample.dao.pojo.Demo2;

/**
 * @author xiezhengchao
 * @since 17/11/7 下午4:44.
 */
public interface DemoService{

    void testMultiWrite(Demo demo, Demo2 demo2, boolean rollback);

    void testRollback(Demo demo, boolean rollback);

    void testPageHelp();

}
