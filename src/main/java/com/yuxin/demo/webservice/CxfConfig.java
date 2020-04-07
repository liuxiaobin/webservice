package com.yuxin.demo.webservice;

import com.yuxin.demo.webservice.impl.HelloYuXinServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @program: webservice
 *
 * @description:
 *
 * @author: xbliu
 *
 * @create: 2019-08-30 16:18
 **/
@Configuration
public class CxfConfig {

    @SuppressWarnings("all")
    @Bean(name = "cxfServlet")
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(),"/demo/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public HelloYuXinService helloYuXinService() {
        return new HelloYuXinServiceImpl();
    }


    /**
     * 注册HelloYuXinService接口到webservice服务
     * @return
     */
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(),helloYuXinService());
        endpoint.publish("/Service");//接口发布在 /Service 目录下
        return endpoint;
    }
}
