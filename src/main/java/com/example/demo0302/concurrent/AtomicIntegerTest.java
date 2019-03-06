package com.example.demo0302.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description:
 */
public class AtomicIntegerTest {

    private static final int THREADS_CONUT = 20;
//    public static int count = 0;//不能保证原子性
//    public static volatile int count = 0;//不能保证原子性
    private static AtomicInteger count = new AtomicInteger(0);//底层实现原理是基于CAS（compare-and-swap）
    /**
     * CAS：即比较并替换，是一种实现并发算法时常用到的技术，Java并发包中的很多类都使用了CAS技术。
     * AtomicInteger.incrementAndGet→unsafe.getAndAddInt→compareAndSwapInt（CAS）
     * AtomicInteger原子操作是通过CAS实现，CAS底层利用unsafe提供了原子性操作方法。
     * CAS存在ABA问题：当一个值从A更新成B，又更新会A，普通CAS机制会误判通过检测。
     * 解决ABA问题：利用版本号比较可以有效解决ABA问题。
     * https://blog.csdn.net/moakun/article/details/79913968  什么是CAS机制？
     * 所谓CAS乐观锁，表现为一组指令，当利用CAS执行试图进行一些更新操作时。会首先比较当前数值，如果数值未变，代表没有其它
     * 线程进行并发修改，则成功更新。如果数值改变，则可能出现不同的选择，要么进行重试，要么就返回是否成功。也就是所谓的“乐观锁”。
     *
     */
    public static void increase() {
//        count++;
        count.incrementAndGet();
        System.out.println(count);
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_CONUT];
        for (int i = 0; i < THREADS_CONUT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }

        while (Thread.activeCount() > 1) { //返回当前线程的线程组中活动线程的数量
            Thread.yield();
        }
        System.out.println(count);
    }
}
