package com.example.demo0302.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description: Semaphore是一个计数信号量，常用于限制可以访问某些资源（物理或逻辑的）线程数目。
 * https://www.cnblogs.com/klbc/p/9500947.html     https://blog.csdn.net/carson0408/article/details/79475723
 */
public class SemaphoreTest {
    private static final Semaphore semaphore=new Semaphore(3,true);//多加一个参数true为公平信号量
    private static final ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    /**
     Semaphore主要方法：
     Semaphore(int permits):构造方法，创建具有给定许可数的计数信号量并设置为非公平信号量。
     Semaphore(int permits,boolean fair):构造方法，当fair等于true时，创建具有给定许可数的计数信号量并设置为公平信号量。
     void acquire():从此信号量获取一个许可前线程将一直阻塞。相当于一辆车占了一个车位。
     void acquire(int n):从此信号量获取给定数目许可，在提供这些许可前一直将线程阻塞。比如n=2，就相当于一辆车占了两个车位。
     void release():释放一个许可，将其返回给信号量。就如同车开走返回一个车位。
     void release(int n):释放n个许可。
     int availablePermits()：当前可用的许可数。

     */
    public static void main(String[] args)
    {
        String[] name= {"李明","王五","张杰","王强","赵二","李四","张三"};
        int[] age= {26,27,33,45,19,23,41};
        for(int i=0;i<7;i++)
        {
            Thread t1=new InformationThread(name[i],age[i]);
            threadPool.execute(t1);
        }
    }
    private static class InformationThread extends Thread{
        private final String name;
        private final int age;
        public InformationThread(String name,int age)
        {
            this.name=name;
            this.age=age;
        }

        public void run()
        {
            try
            {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName()+":大家好，我是"+name+"我今年"+age+"岁当前时间为："+System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(name+"要准备释放许可证了，当前时间为："+System.currentTimeMillis());
                System.out.println("当前可使用的许可数为："+semaphore.availablePermits());
                semaphore.release();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
