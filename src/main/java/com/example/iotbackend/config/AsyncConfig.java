package com.example.iotbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

/**
 * Configuration class to enable asynchronous processing and define a thread pool executor.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Defines a thread pool executor for handling asynchronous tasks.
     *
     * @return the configured Executor.
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        // Create and configure the ThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Minimum number of threads
        executor.setMaxPoolSize(5);  // Maximum number of threads
        executor.setQueueCapacity(500); // Queue capacity for pending tasks
        executor.setThreadNamePrefix("AsyncThread-"); // Prefix for thread names
        executor.initialize();
        return executor;
    }
}
