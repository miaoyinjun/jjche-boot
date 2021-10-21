package com.boot.admin.system.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试用
 *
 * @author Zheng Jie
 * @since 2019-01-08
 * @version 1.0.8-SNAPSHOT
 */
@Slf4j
@Component
public class TestTask {

    /**
     * <p>run.</p>
     */
    public void run(){
        log.info("run 执行成功");
    }

    /**
     * <p>run1.</p>
     *
     * @param str a {@link java.lang.String} object.
     */
    public void run1(String str){
        log.info("run1 执行成功，参数为： {}" + str);
    }

    /**
     * <p>run2.</p>
     */
    public void run2(){
        log.info("run2 执行成功");
    }
}
