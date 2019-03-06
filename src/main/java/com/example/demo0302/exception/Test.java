package com.example.demo0302.exception;

/**
 * @author: yiqq
 * @date: 2019/3/5
 * @description:
 */
public class Test {
    public static void main(String[] args) {
        try {
            int i = 1;
            i = i / 0;
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
