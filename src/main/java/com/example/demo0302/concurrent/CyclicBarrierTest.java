package com.example.demo0302.concurrent;

import javafx.concurrent.Worker;

import java.io.Writer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description: https://www.cnblogs.com/dolphin0520/p/3920397.html
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        testSemaphore();
    }

    public static void testSemaphore(){
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(5); //机器数目
        for(int i=0;i<N;i++)
            new Worker(i,semaphore).start();
    }

    static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        public Worker(int num,Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }
        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人"+this.num+"释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /** CyclicBarrier重用,CountDownLatch无法进行重复使用*/
    public static void testReplaceCyclicBarrier() {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CyclicBarrier重用");
        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
    }
    public static void test() {
        /**如果说想在所有线程写入操作完之后，进行额外的其他操作可以为CyclicBarrier提供Runnable参数：
        当四个线程都到达barrier状态后，会从四个线程中选择一个线程去执行Runnable。*/
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N, () -> System.out.println("当前线程"+Thread.currentThread().getName()));
        for(int i=0;i<N;i++){
            new Writer(barrier).start();
        }
    }

    public static void test1() {
        /**每个写入线程执行完写数据操作之后，就在等待其他线程写入操作完毕。当所有线程线程写入操作完毕之后，所有线程就继续进行后续的操作了。*/
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++){
            new Writer(barrier).start();
        }
    }

    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
