
package com.yuxin.demo.mianshi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @program: webservice
 * @description:
 * @author: xbliu
 * @create: 2020-04-02 17:24
 **/
public class LinkedBlockingQueueDemo {
    static BlockingQueue<String> q1 = new ArrayBlockingQueue(1);
    static BlockingQueue<String> q2 = new ArrayBlockingQueue(1);
    //入队：put(E e)：如果队列满了，一直阻塞，直到队列不满了或者线程被中断-->阻塞
    //出队：take()：如果队列空了，一直阻塞，直到队列不为空或者线程被中断-->阻塞
    public static void main(String[] args) {
        char[] aI = "123456".toCharArray();
        char[] bI = "ABCDEF".toCharArray();
        new Thread(() -> {
            for (char c : aI) {
                System.out.print(c);
                try {
                    q1.put("OK");
                    q2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "t1").start();
        new Thread(() -> {
            for (char c : bI) {
                try {
                    System.out.println("------");
                    q1.take();
                    System.out.println("---------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(c);
                try {
                    q2.put("OK");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "t2").start();


    }
}
