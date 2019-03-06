package com.example.demo0302.concurrent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description:
 */
public class CopyOnWriteArrayListTest {
    private static final int THREAD_POOL_MAX_NUM = 10;
    private List<String> mList = new CopyOnWriteArrayList<>();//用ArrayList会抛出并发异常，在同一时间多个线程无法对同一个List进行读取和增删。
    public static void main(String args[]){
        /**
         CopyOnWriteArrayList处理并发：
         1.add操作：使用ReentrantLock，保证同一时间只能有一个线程在添加元素。然后使用Arrays.copyOf(...)方法复制出
         另一个新的数组，而且新的数组的长度比原来数组的长度+1，副本复制完毕，新添加的元素也赋值添加完毕，最后又
         把新的副本数组赋值给了旧的数组，最后在finally语句块中将锁释放。
         2.remove操作：使用ReentrantLock;删除元素，判断要删除的元素是否最后一个，如果最后一个直接在复制副本数组
         的时候，复制长度为旧数组的length-1即可；但是如果不是最后一个元素，就先复制旧的数组的index前面元素到新数
         组中，然后再复制旧数组中index后面的元素到数组中，最后再把新数组复制给旧数组的引用。
         CopyOnWriteArrayList
         优点：解决的开发工作中的多线程的并发问题
         缺点：内存占有问题(两个数组同时驻扎在内存中)，数据一致性(CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性)
         所以如果你希望写入的的数据，马上能读到，请不要使用CopyOnWrite容器.
         */
        new CopyOnWriteArrayListTest().start();
    }
    private void initData() {
        for(int i = 0 ; i <= THREAD_POOL_MAX_NUM ; i ++){
            this.mList.add("...... Line "+(i+1)+" ......");
        }
    }
    private void start(){
        initData();
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_MAX_NUM);
        for(int i = 0 ; i < THREAD_POOL_MAX_NUM ; i ++){
            service.execute(new ListReader(this.mList));
            service.execute(new ListWriter(this.mList,i));
        }
        service.shutdown();
    }
    private class ListReader implements Runnable{
        private List<String> mList ;
        public  ListReader(List<String> list) {
            this.mList = list;
        }
        @Override
        public void run() {
            if(this.mList!=null){
                for(String str : this.mList){
                    System.out.println(Thread.currentThread().getName()+" : "+ str);
                }
            }
        }
    }
    private class ListWriter implements Runnable{
        private List<String> mList ;
        private int mIndex;
        public  ListWriter(List<String> list,int index) {
            this.mList = list;
            this.mIndex = index;
        }
        @Override
        public void run() {
            if(this.mList!=null){
                //this.mList.remove(this.mIndex);
                this.mList.add("...... add "+mIndex +" ......");
            }
        }
    }
}
