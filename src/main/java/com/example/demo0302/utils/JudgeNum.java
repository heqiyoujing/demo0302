package com.example.demo0302.utils;

/**
 * @author: yiqq
 * @date: 2019/3/7
 * @description: 判断一个数是否是基数
 */
public class JudgeNum {
    public static void main(String[] args) {
        System.out.println(isOdd(-1));
    }
    public static boolean isOdd(int i) {
        /*if (i % 2 == 1) {
            return true;
        } else {
            return false;
        }*/

       /* return i % 2 == 1;*/

       /* return i % 2 == 1 || i % 2 == -1;*/

        /*return i % 2 != 0;*/

        return (i & 1) == 1;
    }
}
