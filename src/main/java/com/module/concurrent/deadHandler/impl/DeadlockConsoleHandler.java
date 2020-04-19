package com.module.concurrent.deadHandler.impl;

import com.module.concurrent.deadHandler.DeadlockPredicate;
import com.module.concurrent.deadHandler.DeadlockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;
import java.util.Arrays;

@Component("ConsoleHandler")
public class DeadlockConsoleHandler implements DeadlockHandler {

    @Autowired
    DeadlockPredicate deadlockPredicate;

    @Override
    public void handleDeadlock(ThreadInfo[] deadlockedThreads) {
        if(deadlockedThreads != null){
            System.err.println("Deadlock detected!");

            Arrays.stream(deadlockedThreads)
                    .filter(deadlockPredicate)
                    .forEach(item -> {
                        System.err.println(item.toString().trim());
                        printStackTraceElement(
                                deadlockPredicate.getStackElements(item.getThreadId()));
                    });
        }
    }

    private void printStackTraceElement(StackTraceElement[] elements) {
        if(elements == null)
            return;
        for(int i = 0; i < elements.length; i++) {
            System.out.println("t" + elements[i].toString().trim());
        }
    }

}
