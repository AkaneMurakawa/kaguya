package com.github.kaguya.controller;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.model.User;
import com.github.kaguya.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SystemController {

    private static final int INTERVAL_SECOND = 3600;

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {

        User user = null;
        // TODO
        // userService.getUser();
        if (null == user) {
            return "login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(CommonConstant.SESSION_LOGIN_USER, user);
        session.setMaxInactiveInterval(INTERVAL_SECOND);
        return "docs/addPage";
    }
}
