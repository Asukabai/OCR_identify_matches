package com.ss.price.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



// http://localhost:28009/ssPrice/web/viewFile
@Controller
public class IndexController {
    @GetMapping("/ssPrice/web/viewFile")
    public String index() {
        System.out.println("http://localhost:28009/ssPrice/web/viewFile");
        System.out.println("前端项目入口地址进入");
         return "/ssPrice/web/index.html";
//        return "/index.html";
    }
}