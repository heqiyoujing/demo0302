package com.example.demo0302.commandLineRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author: yiqq
 * @date: 2019/3/2
 * @description:  SpringBootServletInitializer
 * https://blog.csdn.net/qq_28289405/article/details/81279742
 */

@Order(1)
public class MyWebApplicationInitializer  implements WebApplicationInitializer {
    private Logger logger= LoggerFactory.getLogger(MyWebApplicationInitializer.class);
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("启动加载自定义的MyWebApplicationInitializer");
    }
}
