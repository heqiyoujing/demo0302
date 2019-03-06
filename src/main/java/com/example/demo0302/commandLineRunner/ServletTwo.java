package com.example.demo0302.commandLineRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: yiqq
 * @date: 2019/3/2
 * @description:
 */
public class ServletTwo extends HttpServlet {
    private static final long serialVersionUID = -4186518845701003231L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletTwo");
        resp.setContentType("text/html");
        resp.getWriter().write("ServletTwo");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("ServletTwo loadOnStart");
    }

}
