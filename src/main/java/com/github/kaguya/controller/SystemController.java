package com.github.kaguya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/loginPage")
    public String loginPage(){
        return "login";
    }
}