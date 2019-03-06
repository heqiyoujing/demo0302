package com.example.demo0302.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: yiqq
 * @date: 2019/2/20
 * @description: 使用ExecutorService、Callable、Future实现有返回结果的线程
 */
@SuppressWarnings("unchecked")
public class ExecutorServiceTest {
    /**
     * Executors是一个框架
     * ExecutorService： 真正的线程池接口。
     * ThreadPoolExecutor：->AbstractExecutorService->ExecutorService->Executor， ExecutorService的默认实现
     * ScheduledExecutorService： extends ExecutorService，能和Timer/TimerTask类似，解决那些需要任务重复执行的问题。
     * ScheduledThreadPoolExecutor： extends ThreadPoolExecutor implements ScheduledExecutorService，周期性任务调度的类实现。
     * */
    public static void main(String[] args) throws ExecutionException,
            InterruptedException {
        System.out.println("----程序开始运行----");
        Date date1 = new Date();

        int taskSize = 5;
        // 创建一个线程池,固定数目
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);

        //****************其他创建线程方式********************
       /* ExecutorService pool1 = Executors.newCachedThreadPool();//创建一个可缓存的线程池，调用execute 将重用以前构造的线程（如果线程可用）
        ExecutorService pool2 = Executors.newSingleThreadExecutor();//创建一个单线程化的Executor。
        ExecutorService pool3 = Executors.newScheduledThreadPool(taskSize);//创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类。*/
        //************************************

        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < taskSize; i++) {
            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>>>>" + f.get().toString());
        }

        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
    }

    static class MyCallable implements Callable<Object> {
        private String taskNum;

        MyCallable(String taskNum) {
            this.taskNum = taskNum;
        }

        public Object call() throws Exception {
            System.out.println(">>>" + taskNum + "任务启动");
            Date dateTmp1 = new Date();
            Thread.sleep(1000);
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            System.out.println(">>>" + taskNum + "任务终止");
            return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
        }
    }
}

