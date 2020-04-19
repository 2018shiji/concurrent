package com.module.concurrent.deadDetector;

import com.module.concurrent.deadHandler.DeadlockHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 死锁检测的开销会很大
 */
@Component
@AllArgsConstructor
public class DeadlockDetector {

    private final DeadlockHandler deadlockHandler;
    private final long period;
    private final TimeUnit unit;

    private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    final Runnable deadlockCheck = () -> {
        long[] deadlockedThreadIds = mbean.findDeadlockedThreads();
        if(deadlockedThreadIds != null){
            ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreadIds);
            deadlockHandler.handleDeadlock(threadInfos);
        }
    };

    public void start() {
        scheduler.scheduleAtFixedRate(deadlockCheck, 0, period, unit);
    }

}
