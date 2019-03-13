package com.example.demo0302;

/**
 * @author: yiqq
 * @date: 2019/3/13
 * @description:  ThreadLocal 原理分析
 * https://www.cnblogs.com/dolphin0520/p/3920407.html
 */
public class Document {
    /**
     ThreadLocal 原理分析:
     首先，在每个线程Thread内部有一个ThreadLocal.ThreadLocalMap类型的成员变量threadLocals，这个threadLocals就是用来存储
     实际的变量副本的，键值为当前ThreadLocal变量，value为变量副本（即T类型的变量）。
     初始时，在Thread里面，threadLocals为空，当通过ThreadLocal变量调用get()方法或者set()方法，就会对Thread类中的
     threadLocals进行初始化，并且以当前ThreadLocal变量为键值，以ThreadLocal要保存的副本变量为value，存到threadLocals。
     然后在当前线程里面，如果要使用副本变量，就可以通过get方法在threadLocals里面查找。
     1）实际的通过ThreadLocal创建的副本是存储在每个线程自己的threadLocals中的；
     2）为何threadLocals的类型ThreadLocalMap的键值为ThreadLocal对象，因为每个线程中可有多个threadLocal变量，就像上面代码中
     的longLocal和stringLocal；
     3）在进行get之前，必须先set，否则会报空指针异常；
     如果想在get之前不需要调用set就能正常访问的话，必须重写initialValue()方法。
     因为在上面的代码分析过程中，我们发现如果没有先set的话，即在map中查找不到对应的存储，则会通过调用setInitialValue方法
     返回i，而在setInitialValue方法中，有一个语句是T value = initialValue()， 而默认情况下，initialValue方法返回的是null。
     ThreadLocal的应用场景：
     最常见的ThreadLocal使用场景为 用来解决 数据库连接、Session管理等。
     */
    static ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
    static ThreadLocal<String> stringLocal = new ThreadLocal<String>();
    public static void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }
    public static long getLong() {
        return longLocal.get();
    }
    public static String getString() {
        return stringLocal.get();
    }
    public static void main(String[] args) throws InterruptedException {
        final Document test = new Document();
        Document.set();
        System.out.println("--------------------------1");
        System.out.println(test.getLong());
        System.out.println(test.getString());
        Thread thread1 = new Thread(() -> {
            test.set();
            System.out.println("--------------------------2");
            System.out.println(test.getLong());
            System.out.println(test.getString());
        });
        thread1.start();
        thread1.join();//线程thread1加进来，先执行2，执行完后再执行3
        System.out.println("--------------------------3");
        System.out.println(test.getLong());
        System.out.println(test.getString());
        /**
         最后打印结果为：
         --------------------------1
         1
         main
         --------------------------2
         11
         Thread-0
         --------------------------3
         1
         main

         在main线程中和thread1线程中，longLocal保存的副本值和stringLocal保存的副本值都不一样。
         最后一次在main线程再次打印副本值是为了证明在main线程中和thread1线程中的副本值确实是不同的。
         */
    }
}
