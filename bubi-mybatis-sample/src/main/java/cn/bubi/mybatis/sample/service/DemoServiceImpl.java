package cn.bubi.mybatis.sample.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bubi.common.handler.Page;
import cn.bubi.mybatis.sample.dao.handle.DemoHandle;
import cn.bubi.mybatis.sample.dao.handle.DemoHandle2;
import cn.bubi.mybatis.sample.dao.pojo.Demo;
import cn.bubi.mybatis.sample.dao.pojo.Demo2;

/**
 * @author xiezhengchao
 * @since 17/11/7 下午4:45.
 */
@Service
public class DemoServiceImpl implements DemoService{

    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Resource
    private DemoHandle demoHandle;
    @Resource
    private DemoHandle2 demoHandle2;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testMultiWrite(Demo demo, Demo2 demo2, boolean rollback){

        demoHandle.insert(demo);

        demoHandle2.insert(demo2);

        // 在一个事务下模拟取操作
        Demo readDemo = demoHandle.select(1L);
        logger.info("\n-------------------------------------------------");
        logger.info("readDemo:" + readDemo);

        if (rollback) {
            throw new RuntimeException("rollback exception!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testRollback(Demo demo, boolean rollback){

        demoHandle.insert(demo);

        // 在一个事务下模拟取操作
        Demo readDemo = demoHandle.select(1L);
        logger.info("\n-------------------------------------------------");
        logger.info("readDemo:" + readDemo);

        if (rollback) {
            throw new RuntimeException("rollback exception!");
        }
    }

    @Override
    public void testPageHelp(){

        // 分页插件测试
        Page<Demo> page = new Page<>(1, 5);


        Page<Demo> result = demoHandle.queryListForPage("pageQuery", page);


        logger.info("\n****************************************************************");
        result.getResultList().forEach(System.out:: println);
    }
}
