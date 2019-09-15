package com.yang.tutorial.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author yangz
 */
public class ThreadPoolService {

    private static final int DEFAULT_CORE_SIZE = 100;
    private static final int MAX_QUEUE_SIZE = 200;
    private volatile static ThreadPoolExecutor executor;

    /**
     * 获取单例的线程池对象
     *
     * @param threadPoolName
     * @return
     */
    public static ThreadPoolExecutor getInstance(String threadPoolName) {
        if (executor == null) {
            synchronized (ThreadPoolService.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(
                            // 核心线程数
                            DEFAULT_CORE_SIZE,
                            // 最大线程数
                            MAX_QUEUE_SIZE,
                            // 闲置线程存活时间
                            Integer.MAX_VALUE,
                            // 时间单位
                            TimeUnit.MILLISECONDS,
                            // 线程队列
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),
                            // 线程工厂
                            new ThreadFactoryBuilder().setNameFormat(threadPoolName).build()
                    );
                }
            }
        }
        return executor;
    }


}
