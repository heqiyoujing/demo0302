package com.example.demo0302.concurrent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description: https://blog.csdn.net/linchunquan/article/details/22382487
 */
public class FutureTaskTest {
    private Map<String, Connection> connectionPool = new HashMap<String, Connection>();
    private ReentrantLock lock = new ReentrantLock();//可重入锁
    public static void main(String[] args) {

        /**
         FutureTask实现了Runnable和Future接口：
         （1）单独使用Runnable时：无法获得返回值
         （2）单独使用Callable时：无法在新线程中(new Thread(Runnable r))使用，只能使用ExecutorService，Thread类只支持Runnable
         （3）FutureTask：实现了Runnable和Future，所以兼顾两者优点，既可以使用ExecutorService，也可以使用Thread
         FutureTask可用于异步获取执行结果或取消执行任务的场景。通过传入Runnable或者Callable的任务给FutureTask，直接调用
         其run方法或者放入线程池执行，之后可以在外部通过FutureTask的get方法异步获取执行结果，因此，FutureTask非常适合用
         于耗时的计算，主线程可以在完成自己的任务后，再去获取结果。另外，FutureTask还可以确保即使调用了多次run方法，它都
         只会执行一次Runnable或者Callable任务，或者通过cancel取消FutureTask的执行等。

         Future有个get方法而获取结果只有在计算完成时获取，否则会一直阻塞直到任务转入完成状态，然后会返回结果或者抛出异常。
         */
        FutureTaskMore();

    }
    /**
     2.FutureTask在高并发环境下确保任务只执行一次
     在很多高并发的环境下，往往我们只需要某些任务只执行一次。这种使用情景FutureTask的特性恰能胜任。举一个例子，假设有一个
     带key的连接池，当key存在时，即直接返回key对应的对象；当key不存在时，则创建连接。对于这样的应用场景，通常采用的方法为
     使用一个Map对象来存储key和连接池对应的对应关系
     */
    public Connection getConnection(String key){
        try{
            lock.lock();
            if(connectionPool.containsKey(key)){
                return connectionPool.get(key);
            }
            else{
                //创建 Connection
                Connection conn = createConnection();
                connectionPool.put(key, conn);
                return conn;
            }
        }
        finally{
            lock.unlock();
        }
    }
    //创建Connection
    private Connection createConnection(){
        return null;
    }

    /**
     *  1.FutureTask执行多任务计算的使用场景：利用FutureTask和ExecutorService，可以用多线程的方式提交计算任务，主线程继续
     *  执行其他任务，当主线程需要子线程的计算结果时，在异步获取子线程的执行结果。
     */
    public static void FutureTaskMore() {
        FutureTaskTest futureTaskTest=new FutureTaskTest();
        // 创建任务集合
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        // 创建线程池
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            // 传入Callable对象创建FutureTask对象
            FutureTask<Integer> ft = new FutureTask<Integer>(futureTaskTest.new ComputeTask(i, ""+i));
            taskList.add(ft);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            exec.submit(ft);
        }

        System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");

        // 开始统计各计算线程计算结果
        Integer totalResult = 0;
        for (FutureTask<Integer> ft : taskList) {
            try {
                //FutureTask的get方法会自动阻塞,直到获取计算结果为止
                totalResult = totalResult + ft.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        exec.shutdown();
        System.out.println("多任务计算后的总结果是:" + totalResult);
    }

    private class ComputeTask implements Callable<Integer> {

        private Integer result = 0;
        private String taskName = "";

        public ComputeTask(Integer iniResult, String taskName){
            result = iniResult;
            this.taskName = taskName;
            System.out.println("生成子线程计算任务: "+taskName);
        }

        public String getTaskName(){
            return this.taskName;
        }

        @Override
        public Integer call() throws Exception {
            // TODO Auto-generated method stub

            for (int i = 0; i < 100; i++) {
                result =+ i;
            }
            // 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果是等待直至完成。
            Thread.sleep(5000);
            System.out.println("子线程计算任务: "+taskName+" 执行完成!");
            return result;
        }
    }

    /**
         Future是一个接口，FutureTask是Future的一个实现类，并实现了Runnable，因此FutureTask可以传递到线程对象Thread中新
     建一个线程执行。所以可以通过Excutor(线程池)来执行，也可传递给Thread对象执行。
         如果在主线程中需要执行比较耗时的操作，但又不想阻塞主线程时，可以把这些作业交给Future对象在后台完成，当主线程将
     来需要时，就可以通过Future对象获得后台作业的计算结果或者执行状态。
         FutureTask是为了弥补Thread的不足而设计的，它可以让程序员准确地知道线程什么时候执行完成并获得到线程执行完成后返
     回的结果（如果有需要）。
         FutureTask是一种可以取消的异步的计算任务。它的计算是通过Callable实现的，它等价于可以携带结果的Runnable，并且有
     三个状态：等待、运行和完成。完成包括所有计算以任意的方式结束，包括正常结束、取消和异常。
         Executor框架利用FutureTask来完成异步任务，并可以用来进行任何潜在的耗时的计算。一般FutureTask多用于耗时的计算，
     主线程可以在完成自己的任务后，再去获取结果。
     */
}
