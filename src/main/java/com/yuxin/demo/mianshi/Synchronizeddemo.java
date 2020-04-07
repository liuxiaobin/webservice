/**
 * Copyright (C), 2019-2020, 宇信融汇
 * FileName: Synchronizeddemo
 * Author:   xbliu
 * Date:     2020/4/3 16:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * xbliu           修改时间           版本号              描述
 */
package com.yuxin.demo.mianshi;/**
 * @Classname Synchronizeddemo
 * @Description TODO
 * @Date 2020/4/3 16:44
 * @Created by xbliu
 */

/**
 * @program: webservice
 *
 * @description:
 *
 * @author: xbliu
 *
 * @create: 2020-04-03 16:44
 **/
public class Synchronizeddemo {
    public static void main(String[] args) {
        final Object o = new Object();
        char[] aI = "123456".toCharArray();
        char[] bI = "ABCDEF".toCharArray();
        new Thread(()->{
            synchronized(o){
                for (char c : aI) {
                    System.out.println(c);

                }
            }
        }

        ).start();
    }



}
