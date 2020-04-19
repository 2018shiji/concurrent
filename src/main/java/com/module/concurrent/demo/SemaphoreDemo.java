package com.module.concurrent.demo;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

/**
 * 锁一般是互斥排他的，而信号量可以认为是一个共享锁
 * 允许N个线程同时进入临界区，但是超出许可范围的只能等待
 * 如果N=1，则类似于lock
 */
@Component
public class SemaphoreDemo implements Runnable {
    final Semaphore semaphore = new Semaphore(5);

    @Override
    public void run() {
        try {
            semaphore.acquire();
            //模拟线程耗时操作
            Thread.sleep(2000L);
            System.out.println("Job done! " + Thread.currentThread().getId());
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }


}
