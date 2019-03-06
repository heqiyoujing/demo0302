package com.example.demo0302.concurrent.WriteBlockingQuece;

/**
 * @author: yiqq
 * @date: 2019/3/6
 * @description:  写自己的阻塞队列
 * https://blog.csdn.net/jinjin603/article/details/81868993
 */
public class Test {
    public static void main(String[] args) {
        SimpleBlockingQueue sbq = new SimpleBlockingQueue();
        Thread t1 = new Thread() {
            public void run() {
                try {
                    sbq.add(1);
                    Thread.sleep(1000);
                    sbq.add(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                try {
                    while (true) {
                        Integer item = sbq.take();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();

    }
}
