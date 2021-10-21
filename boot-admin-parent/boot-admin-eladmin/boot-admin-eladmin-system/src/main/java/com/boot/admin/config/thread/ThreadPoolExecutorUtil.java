package com.boot.admin.config.thread;

import com.boot.admin.property.AdminProperties;
import com.boot.admin.property.AsyncTaskPoolProperties;
import com.boot.admin.core.util.SpringContextHolder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取自定义线程池
 *
 * @author Zheng Jie
 * @since 2019年10月31日18:16:47
 * @version 1.0.8-SNAPSHOT
 */
public class ThreadPoolExecutorUtil {

    /**
     * <p>getPoll.</p>
     *
     * @return a {@link java.util.concurrent.ThreadPoolExecutor} object.
     */
    public static ThreadPoolExecutor getPoll(){

        AdminProperties properties = SpringContextHolder.getBean(AdminProperties.class);
        AsyncTaskPoolProperties poolProperties = properties.getTask().getPool();
        return new ThreadPoolExecutor(
                poolProperties.getCorePoolSize(),
                poolProperties.getMaxPoolSize(),
                poolProperties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(poolProperties.getQueueCapacity()),
                new TheadFactoryName()
        );
    }
}
