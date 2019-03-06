package com.example.demo0302.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yiqq
 * @date: 2019/3/2
 * @description:  为什么重写equals一定要重写hashcode？
 */
public class HashMapTest {
    private int a;

    public HashMapTest(int a) {
        this.a = a;
    }

    public static void main(String[] args) {
        /**
         * 两个对象，一模一样，但是因为没有重写hashcode()和equals(),如果用对象作map的key,取不到值。
         * 如果非得用对象作key，需要重写对象的hashcode()和equals()
         * HashMap的原理，HashMap存储数据的时候，是取的key值的哈希值，然后计算数组下标，采用链地址法解决冲突，
         然后进行存储；取数据的时候，依然是先要获取到hash值，找到数组下标，然后for遍历链表集合，进行比较是否
         有对应的key。比较关心的有2点：1.不管是put还是get的时候，都需要得到key的哈希值，去定位key的数组下标；
         2.在get的时候，需要调用equals方法比较是否有相等的key存储过。
         */
        Map<HashMapTest, Integer> map = new HashMap<HashMapTest, Integer>();
        HashMapTest instance = new HashMapTest(1);
        HashMapTest newInstance = new HashMapTest(1);
        System.out.println("instance.hashcode:" + instance.hashCode());
        System.out.println("newInstance.hashcode:" + newInstance.hashCode());
        map.put(instance, 1);
        map.put(newInstance, 2);
        Integer value = map.get(instance);
        System.out.println("instance value:"+value);
        Integer value1 = map.get(newInstance);
        System.out.println("newInstance value:"+value1);

    }
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof HashMapTest)) {
            return false;
        } else {
            HashMapTest other = (HashMapTest)o;
            if(!other.canEqual(this)) {
                return false;
            } else {
                Integer this$data = this.getA();
                Integer other$data = other.getA();
                if(this$data == null) {
                    if(other$data != null) {
                        return false;
                    }
                } else if(!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + a;
//        result = prime * result + (name == null) ? 0 : name.length.hashCode();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof HashMapTest;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getA() {
        return a;
    }
}
