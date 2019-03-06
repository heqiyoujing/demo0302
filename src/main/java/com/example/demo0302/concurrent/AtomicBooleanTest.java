package com.example.demo0302.concurrent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description: AtomicReference  https://www.cnblogs.com/skywang12345/p/3514623.html
 * AtomicReference的源码比较简单。它是通过"volatile"和"Unsafe提供的CAS函数实现"原子操作。
 *(01) value是volatile类型。这保证了：当某线程修改value的值时，其他线程看到的value值都是最新的value值，即修改之后的volatile的值。
 *(02) 通过CAS设置value。这保证了：当某线程池通过CAS函数(如compareAndSet函数)设置value时，它的操作是原子的，即线程在操作value时不会被中断。
 */
public class AtomicBooleanTest {
    private static volatile AtomicBoolean atomicBoolean = new AtomicBoolean(true);

    public void executeBusiLogic(){
        if( atomicBoolean.compareAndSet(true, false) ){
            try{
                System.out.println(LocalDate.now() + " " + LocalTime.now() + "--" + Thread.currentThread().getName() + "--处理业务逻辑开始...");
                Thread.sleep(5000);
                System.out.println(LocalDate.now() + " " + LocalTime.now() + "--" + Thread.currentThread().getName() + "--处理业务逻辑完毕.");
            }catch(Exception e){
                System.out.println(LocalDate.now() + " " + LocalTime.now() + "--" + Thread.currentThread().getName() + "--处理业务逻辑失败!!!");
            }finally{
                atomicBoolean.set(true);
            }
        }else{
            System.out.println(LocalDate.now() + " " + LocalTime.now() + "--" + Thread.currentThread().getName() + "--已经存在处理中的业务，请稍后再试!");
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AtomicBooleanTest demo = new AtomicBooleanTest();
        for(int i = 0; i < 10; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    demo.executeBusiLogic();
                }
            });
        }
        executorService.shutdown();
        /**
         * 始终thread-1首先获得操作权限
         */
    }
}
