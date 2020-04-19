package com.module.concurrent.reentrant;

import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.cnblogs.com/xdecode/p/9102741.html
 * 可重入锁：允许限时获取锁（tryLock），允许优先响应中断
 * 公平锁
 */
public class DeadLockByReentrant implements Runnable {

    int lock;
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();

    public DeadLockByReentrant(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try{
            if(lock == 1){
                lock1.lockInterruptibly();
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {e.printStackTrace();}
                lock2.lockInterruptibly();
            } else {
                lock2.lockInterruptibly();
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {e.printStackTrace();}
                lock1.lockInterruptibly();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(lock1.isHeldByCurrentThread())
                lock1.unlock();
            if(lock2.isHeldByCurrentThread())
                lock2.unlock();
            System.out.println(Thread.currentThread().getId() + "线程中断");
        }

    }
}
