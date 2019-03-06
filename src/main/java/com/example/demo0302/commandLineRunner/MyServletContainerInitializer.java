package com.example.demo0302.commandLineRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * @author: yiqq
 * @date: 2019/3/2
 * @description:
 */
public class MyServletContainerInitializer implements ServletContainerInitializer {

    private Logger logger= LoggerFactory.getLogger(MyServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        logger.info("启动加载自定义的MyServletContainerInitializer");
        System.out.println("启动加载自定义的MyServletContainerInitializer");
        ServletRegistration.Dynamic testServlet=servletContext.addServlet("servlet4","com.shf.springboot.servlet.Servlet4");
        testServlet.setLoadOnStartup(1);
        testServlet.addMapping("/servlet4");
    }
}
