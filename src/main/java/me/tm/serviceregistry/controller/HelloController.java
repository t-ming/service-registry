package me.tm.serviceregistry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping(value = "/hello", name = "HelloService")
    public String hello() {
        return "hello!";
    }
}