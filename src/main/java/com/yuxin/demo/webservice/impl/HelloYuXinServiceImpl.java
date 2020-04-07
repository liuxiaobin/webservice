/**
 * Copyright (C), 2019-2019, 宇信融汇
 * FileName: HelloYuXinServiceImpl
 * Author:   xbliu
 * Date:     2019/8/17 17:09
 * Description: 你好宇信websevice测试接口
 * History:
 * <author>          <time>          <version>          <desc>
 * xbliu           修改时间           版本号              描述
 */
package com.yuxin.demo.webservice.impl;/**
 * @Classname HelloYuXinServiceImpl
 * @Description TODO
 * @Date 2019/8/17 17:09
 * @Created by xbliu
 */

import com.yuxin.demo.webservice.HelloYuXinService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * @program: webservice
 * @description:
 * @author: xbliu
 * @create: 2019-08-17 17:09
 **/

@WebService(serviceName = "HelloYuXinService"//服务名
        , targetNamespace = "http://webservice.demo.yuxin.com",//报名倒叙，并且和接口定义保持一致
        endpointInterface = "com.yuxin.demo.webservice.HelloYuXinService"// 接口地址
)//包名
@Component
public class HelloYuXinServiceImpl implements HelloYuXinService {
    @Override
    public String sayHello(String name) {
        return "大家好，我是" + name;
    }
}
