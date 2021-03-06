package cn.bubi.mybatis.sample;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.bubi.mybatis.sample.dao.pojo.Demo;
import cn.bubi.mybatis.sample.dao.pojo.Demo2;
import cn.bubi.mybatis.sample.service.DemoService;

/**
 * @author xiezhengchao
 * @since 17/11/7 下午4:27.
 */
@SpringBootApplication
public class SampleRun implements CommandLineRunner{

    private DemoService demoService;

    public SampleRun(DemoService demoService){
        this.demoService = demoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleRun.class, args);
    }

    @Override
    public void run(String... args) throws Exception{

        Demo demo = new Demo();
        demo.setAge(33);
        demo.setName("write1:" + Instant.now());


        Demo2 demo2 = new Demo2();
        demo2.setAge(44);
        demo2.setName("write2:" + Instant.now());

        demoService.testMultiWrite(demo, demo2, true);

        //        rollback();

        //        demoService.testPageHelp();
        //        demoService.testPageHelp();
        //        demoService.testPageHelp();

    }

    private void rollback(){
        Demo demo = new Demo();
        demo.setAge(18);
        demo.setName("write db no rollback:" + Instant.now());
        demoService.testRollback(demo, false);

        //        Demo rollbackDemo = new Demo();
        //        rollbackDemo.setAge(22);
        //        rollbackDemo.setName("write db rollback:" + Instant.now());
        //        demoService.testRollback(rollbackDemo, true);
    }
}
