
package com.yuxin.demo.mianshi;

/**
 * @program: webservice
 * @description:
 * @author: xbliu
 * @create: 2020-04-02 15:41
 **/
public class ReadyToRunDemo {
    enum ReadyToRun {T1, T2}

//    保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。（实现可见性）
//    禁止进行指令重排序。（实现有序性）
//    volatile 只能保证对单次读/写的原子性。i++ 这种操作不能保证原子性。

    static volatile ReadyToRun r = ReadyToRun.T1;

    public static void main(String[] args) {

        char[] aI = "123456".toCharArray();
        char[] bI = "ABCDEF".toCharArray();
        new Thread(() -> {
            for (char c : aI) {
               while (r!=ReadyToRun.T1){}
                System.out.print(c);
                r = ReadyToRun.T2;
            }
        }, "t1").start();
        new Thread(() -> {
            for (char c : bI) {
                while (r!=ReadyToRun.T2){}
                System.out.print(c);
                r = ReadyToRun.T1;
            }
        },"t2").start();
    }
}
