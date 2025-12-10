package com.zs.common.core.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @NotNull
    @Bean(name = "threadPoolExecutor")
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数。通常情况下设置为CPU核心数*1。若是IO密集型，cpu核心数*2，若是cpu密集型，cpu核心数*1
        // 核心线程会一直存活，及时没有任务需要执行
        //设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
        // 注意：当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理

        // 获取可用处理器的Java虚拟机的数量
        int num = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(num);
        // 设置最大线程数。通常情况下设置为CPU核心数*2
        executor.setMaxPoolSize(num * 2);
        // 设置队列容量
        executor.setQueueCapacity(100);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("zs-thread-");
        // 设置拒绝策略
        // 有4中策略，分别是。
        // ThreadPoolExecutor.AbortPolicy：丢弃任务并抛出RejectedExecutionException异常。
        // ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
        // ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        // ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
