package com.github.kaguya.controller;

import com.github.kaguya.config.SessionCookieContainer;
import com.github.kaguya.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SystemController {

    @Resource
    private UserService userService;
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

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        sessionCookieContainer.clear(request, response);
        return "index";
    }

}
