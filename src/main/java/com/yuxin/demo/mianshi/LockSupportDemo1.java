package com.yuxin.demo.mianshi;


import java.util.concurrent.locks.LockSupport;

/**
 * @program: webservice
 * @description:
 * @author: xbliu
 * @create: 2020-04-02 11:19
 **/
public class LockSupportDemo1 {
    static Thread tone = null, ttwo = null;


    public static void main(String[] args) {
        char[] aI = "123456".toCharArray();
        char[] bI = "ABCDEF".toCharArray();
        tone = new Thread(() -> {
            for (char c : aI) {
                System.out.print(c);
                LockSupport.unpark(ttwo);
                LockSupport.park();
            }
        },"tone");
        ttwo = new Thread(() -> {
            for (char c : bI) {
                LockSupport.park();
                System.out.print(c);
                LockSupport.unpark(tone);
            }
        },"ttwo");
        tone.start();
        ttwo.start();
    }
}

