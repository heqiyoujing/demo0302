package com.example.demo0302;

import com.alibaba.fastjson.JSON;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description:
 */
public class Test {

   static class Person{
        private int age;
        private String name;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public int getAge() {
           return age;
       }

       public void setAge(int age) {
           this.age = age;
       }
   }

    public static void main(String[] args) {
        Person person = new Person();
        person.setName("wow");
        person.setAge(20);
        String str = JSON.toJSONString(person);//将json对象转换为json字符串
        System.out.println(str);
    }
}
