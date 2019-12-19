package com.github.kaguya.controller;

import com.github.kaguya.config.SessionCookieContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SystemController {

    @Resource
    private SessionCookieContainer sessionCookieContainer;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    /**
     * 退出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        sessionCookieContainer.clear(request, response);
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

}
