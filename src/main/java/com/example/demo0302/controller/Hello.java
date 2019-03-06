package com.example.demo0302.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yiqq
 * @date: 2019/3/2
 * @description:
 */
@RestController
public class Hello {

    @RequestMapping("/hello")
    public String test(){
        return "hello world!";
    }
}
