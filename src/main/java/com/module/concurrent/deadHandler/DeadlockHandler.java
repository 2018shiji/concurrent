package com.module.concurrent.deadHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;

@Component("IDeadlockHandler")
@FunctionalInterface
public interface DeadlockHandler {

    void handleDeadlock(final ThreadInfo[] deadlockedThreads);
}
