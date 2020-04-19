package com.module.concurrent.deadHandler;

import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class DeadlockPredicate implements Predicate<ThreadInfo> {

    Map<Thread, StackTraceElement[]> stackTraceMap;
    Map<Long, Thread> threadMap = new HashMap<>();

    @Override
    public boolean test(ThreadInfo threadInfo) {
        stackTraceMap = Thread.getAllStackTraces();
        stackTraceMap.keySet().stream().forEach(item -> {
            threadMap.put(item.getId(), item);
        });
        if(threadInfo != null && threadMap.containsKey(threadInfo.getThreadId()))
            return true;
        return false;
    }

    public StackTraceElement[] getStackElements(long threadId) {
        if(stackTraceMap == null)
            return null;
        return stackTraceMap.get(threadMap.get(threadId));
    }

    public Thread getDeadThead(long threadId) {
        if(stackTraceMap == null)
            return null;
        return threadMap.get(threadId);
    }
}
