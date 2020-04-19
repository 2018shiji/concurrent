package com.module.concurrent;

import com.module.concurrent.deadDetector.DeadlockDetector;
import com.module.concurrent.deadHandler.DeadlockHandler;
import com.module.concurrent.demo.ConditionDemo;
import com.module.concurrent.reentrant.DeadLockByReentrant;
import com.module.concurrent.demo.SemaphoreDemo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ConcurrentApplicationTests {

    DeadlockDetector deadlockDetector;
    @Autowired
    @Qualifier("ConsoleHandler")
    DeadlockHandler consoleHandler;

    @Autowired
    @Qualifier("InterruptHandler")
    DeadlockHandler interruptHandler;

    @Autowired
    SemaphoreDemo semaphoreDemo;

    @Autowired
    ConditionDemo conditionDemo;

    @Test
    void contextLoads() {
        deadlockDetector = new DeadlockDetector(consoleHandler, 5, TimeUnit.SECONDS);
        deadlockDetector.start();

        final Object lock1 = new Object();
        final Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1){
                System.out.println("Thread1 acquired lock1");
                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                }catch(InterruptedException e){e.printStackTrace();}
                synchronized (lock2){
                    System.out.println("Thread1 acquired lock2");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2){
                System.out.println("Thread2 acquired lock2");
                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                }catch(InterruptedException e){e.printStackTrace();}
                synchronized (lock1){
                    System.out.println("Thread2 acquired lock1");
                }
            }
        }).start();
    }

    @Test
    void testReentrantLock(){
        //死锁检查，触发中断
        deadlockDetector = new DeadlockDetector(interruptHandler, 5, TimeUnit.SECONDS);
        deadlockDetector.start();

        DeadLockByReentrant deadLock1 = new DeadLockByReentrant(1);
        DeadLockByReentrant deadLock2 = new DeadLockByReentrant(2);

        Thread t1 = new Thread(deadLock1);
        Thread t2 = new Thread(deadLock2);
        t1.start();
        t2.start();

    }

    /**
     * 模拟20个线程，但是信号量只设置了5个许可
     * 因此线程是按序每2秒5个的打印"job done"
     */
    @Test
    void testSemaphore() {
        ExecutorService service = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 20; i++) {
            service.submit(semaphoreDemo);
        }
    }

    @Test
    void testCondition() {
        Thread thread = new Thread(conditionDemo);
        thread.start();
        try {
            Thread.sleep(2000);
        }catch(InterruptedException e) {e.printStackTrace();}

        System.out.println("after wait, signal!");
        /**通知线程继续执行，唤醒同样需要重新获取锁**/
        ConditionDemo.lock.lock();
        ConditionDemo.condition.signal();
        ConditionDemo.lock.unlock();

    }

    @Test
    void test(){

    }
}
