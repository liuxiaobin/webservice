/**
 * Copyright (C), 2019-2019, 宇信融汇
 * FileName: HelloController
 * Author:   xbliu
 * Date:     2019/8/17 17:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * xbliu           修改时间           版本号              描述
 */
package com.yuxin.demo.controller;/**
 * @Classname HelloController
 * @Description TODO
 * @Date 2019/8/17 17:04
 * @Created by xbliu
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: webservice
 *
 * @description:
 *
 * @author: xbliu
 *
 * @create: 2019-08-17 17:04
 **/
@RestController
public class HelloController {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String sayHello(){
        return "Hello! I am HappyBKs, from Oschina! ";
    }
}
