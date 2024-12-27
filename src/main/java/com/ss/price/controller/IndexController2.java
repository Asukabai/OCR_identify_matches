package com.ss.price.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController2 {
    @GetMapping("/a")
    public String index() {
        System.out.println("12313213213");
        return "1232313";
    }
}