package com.example.demo0302.concurrent;
import java.util.concurrent.CountDownLatch;
/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description: CountDownLatch
 * CountDownLatch的简单理解:  https://blog.csdn.net/joenqc/article/details/76794356
 * CountDownLatch的源码：https://blog.csdn.net/m0_37343985/article/details/83033858
 * CountDownLatch的妙用：https://blog.csdn.net/figo0423/article/details/80604921
 * AbstractQueuedSynchronizer（AQS）: https://www.cnblogs.com/go2sea/p/5618628.html
 */
public class countDownLatchTest {
    /**
     CountDownLatch的用法：
     （1）CountDownLatch典型用法1：某一线程在开始运行前等待n个线程执行完毕。将CountDownLatch的计数器初始化为n new CountDownLatch(n) ，
     每当一个任务线程执行完毕，就将计数器减1 countdownlatch.countDown()，当计数器的值变为0时，在CountDownLatch上 await() 的线程
     就会被唤醒。一个典型应用场景就是启动一个服务时，主线程需要等待多个组件加载完毕，之后再继续执行。
     （2）CountDownLatch典型用法2：实现多个线程开始执行任务的最大并行性。注意是并行性，不是并发，强调的是多个线程在某一时刻同时开始执行。
     类似于赛跑，将多个线程放到起点，等待发令枪响，然后同时开跑。做法是初始化一个共享的CountDownLatch(1)，将其计数器初始化为1，多个线程
     在开始执行任务前首先 coundownlatch.await()，当主线程调用 countDown() 时，计数器变为0，多个线程同时被唤醒。
     CountDownLatch的不足：
     CountDownLatch是一次性的，计数器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，当CountDownLatch使用完毕后，它不能再次被使用。
     CountDownLatch常用的两个方法：
     (1)CountDownLatch.countDown(): → sync.releaseShared →AQS(AbstractQueuedSynchronizer)的tryReleaseShared(arg)、doReleaseShared()
     (2)CountDownLatch.await(): → sync.tryAcquireSharedNanos →AQS的tryReleaseShared(arg)、doAcquireSharedNanos(arg, nanosTimeout)
     AQS定义：AQS是AbustactQueuedSynchronizer的简称，它是一个Java提高的底层同步工具类，用一个int类型的变量表示同步状态，并提供了一系列的
     CAS操作来管理这个同步状态。AQS的主要作用是为Java中的并发同步组件提供统一的底层支持，例如ReentrantLock，CountdowLatch就是基于AQS
     实现的，用法是通过继承AQS实现其模版方法，然后将子类作为同步组件的内部类。
     */

    /**
     * main方法
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        downLatch();
//        testWaitThread();
//        testConcurrent();
    }
    private static long startTime = 0L;
    /**
     * 2、模拟高并发
     */
    public static void testConcurrent(){
        try {
            final int THREAD_NUM = 100;
            startTime = System.currentTimeMillis();
            System.out.println("CountDownLatch started at: " + startTime);
            // 初始化计数器为1
            CountDownLatch countDownLatch = new CountDownLatch(1);
            for (int i = 0; i < THREAD_NUM; i ++) {
                new Thread(new Run(countDownLatch)).start();
            }
            // 启动多个线程
            countDownLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程类
     */
    private static class Run implements Runnable{
        private final CountDownLatch startLatch;
        public Run(CountDownLatch startLatch) {
            this.startLatch = startLatch;
        }
        @Override
        public void run() {
            try {
                // 线程等待
                startLatch.await();
                // 模拟耗时操作
                Thread.sleep(3000);
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " ended at: " + endTime + ", cost: " + (endTime - startTime) + " ms.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1、模拟所有子线程都执行完成后再执行主线程，countdownLatch计数，模拟子线程执行完成之后再执行主线程， 这个也可以用future来实现
     */
    public static void testWaitThread(){
        final CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                Thread.sleep(3000);
                System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                Thread.sleep(3000);
                System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 0.CountDownLatch的test
     * @throws InterruptedException
     */
    public static void downLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    countDownLatch.await();
                    long endTime = System.currentTimeMillis();
                    System.out.println("awaitTime : " + (endTime - startTime) / 1000 + "seconds");//awaitTime : 5seconds
                } catch (InterruptedException e) {}

            }
        }).start();
        Thread.sleep(5000);
        countDownLatch.countDown();//countDownLatch.await()开始等待，等待sleep5分钟之后countDownLatch.countDown()，计数器变为0，线程开始执行
    }
}
