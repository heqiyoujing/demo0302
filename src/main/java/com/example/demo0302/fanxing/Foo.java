package com.example.demo0302.fanxing;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: yiqq
 * @date: 2019/3/7
 * @description: 泛型擦除
 * https://www.cnblogs.com/xll1025/p/6489088.html
 */
public class Foo {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //****************** 1 *********************
        List<String> ls = new ArrayList<String>();
        ls.add("abc");
        List<Integer> li = new ArrayList<Integer>();
        li.add(123);
        System.out.println(ls.getClass() == li.getClass());//true  ，List<String> 和 List<Integer> 擦除后的类型都是 List。
        System.out.println(li instanceof List);//true

        //****************** 2 *********************
        ArrayList<Integer> arrayList3=new ArrayList<Integer>();
        arrayList3.add(1);//这样调用add方法只能存储整形，因为泛型类型的实例为Integer
        /**当我们利用反射调用add方法的时候，却可以存储字符串。这说明了Integer泛型实例在编译之后被擦除了，只保留了原始类型。*/
        arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
        for (int i=0;i<arrayList3.size();i++) {
            System.out.println(arrayList3.get(i));//输出结果： 1  asd
        }

        //****************** 3 *********************
        ArrayList arrayList=new ArrayList();
        arrayList.add(1);
        arrayList.add("121");
        arrayList.add(new Date());
        for (int i=0;i<arrayList.size();i++) {
            System.out.println(arrayList.get(i));
        }

        //****************** 4 *********************
        /**new ArrayList()只是在内存中开辟一个存储空间，可以存储任何的类型对象。而真正涉及类型检查的是它的引用，
         因为我们是使用它引用arrayList2 来调用它的方法，比如说调用add()方法。所以arrayList1引用能完成泛型类型的检查。*/
        ArrayList<String> arrayList1=new ArrayList();
        arrayList1.add("1");//编译通过
        /**arrayList1.add(1);//编译错误*/
        String str1=arrayList1.get(0);//返回类型就是String

        ArrayList arrayList2=new ArrayList<String>();
        arrayList2.add("1");//编译通过
        arrayList2.add(1);//编译通过
        Object object=arrayList2.get(0);//返回类型就是Object

        new ArrayList<String>().add("11");//编译通过
       /** new ArrayList<String>().add(22);//编译错误*/
        String string=new ArrayList<String>().get(0);//返回类型就是String

        //****************** 5 *********************
        /**ArrayList<String> arrayList1=new ArrayList<Object>();//编译错误
        ArrayList<Object> arrayList2=new ArrayList<String>();//编译错误*/
        ArrayList<Object> arrayList_3=new ArrayList<Object>();
        arrayList_3.add(new Object());
        arrayList_3.add(new Object());
        ArrayList<String> arrayList_4=arrayList1;//编译错误
    }
}
