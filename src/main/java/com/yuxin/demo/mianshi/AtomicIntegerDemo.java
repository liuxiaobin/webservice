package com.yuxin.demo.mianshi;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: webservice
 * @description:
 * @author: xbliu
 * @create: 2020-04-02 16:48
 **/
public class AtomicIntegerDemo {
    //确保原子性
    static AtomicInteger r=new AtomicInteger(1);

    public static void main(String[] args) {
        char[] aI = "123456".toCharArray();
        char[] bI = "ABCDEF".toCharArray();
        new Thread(() -> {
            for (char c : aI) {
                while (r.get()!=1){}
                System.out.print(c);
                r.set(2);
            }
        }, "t1").start();
        new Thread(() -> {
            for (char c : bI) {
                while (r.get()!=2){}
                System.out.print(c);
                r.set(1);
            }
        },"t2").start();
    }
}
