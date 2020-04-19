package com.module.concurrent.deadHandler.impl;

import com.module.concurrent.deadHandler.DeadlockPredicate;
import com.module.concurrent.deadHandler.DeadlockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;
import java.util.Arrays;

@Component("InterruptHandler")
public class DeadlockInterruptHandler implements DeadlockHandler {

    @Autowired
    DeadlockPredicate deadlockPredicate;

    @Override
    public void handleDeadlock(ThreadInfo[] deadlockedThreads) {
        if(deadlockedThreads != null) {
            System.err.println("Deadlock detected!");
            Arrays.stream(deadlockedThreads)
                    .filter(deadlockPredicate)
                    .forEach(threadInfo -> {
                        deadlockPredicate.getDeadThead(threadInfo.getThreadId()).interrupt();
                    });
        }
    }
}
