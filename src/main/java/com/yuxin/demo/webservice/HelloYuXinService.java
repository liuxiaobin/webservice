/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: HelloService
 * Author:   xbliu
 * Date:     2019/8/17 17:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * xbliu           修改时间           版本号              描述
 */
package com.yuxin.demo.webservice;

import javax.jws.WebService;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author xbliu
 * @create 2019/8/17
 * @since 1.0.0
 */
@WebService(name="HelloYuXinService",targetNamespace = "http://webservice.demo.yuxin.com")// 命名空间,一般是接口的包名倒序
public interface HelloYuXinService {
    public String sayHello(String name);
}
