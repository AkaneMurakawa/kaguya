package com.github.kaguya.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.kaguya.config.SystemProperty;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.model.User;
import com.github.kaguya.service.AdminOAuthService;
import com.github.kaguya.service.UserService;
import com.github.kaguya.util.SecurityUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 第三方登录 Controller
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {

    private final AuthRequestFactory factory;
    private static final String PASSWORD_PREFIX = "kaguya";

    @Resource
    private UserService userService;
    @Resource
    private AdminOAuthService adminOAuthService;

    /**
     * 获取系统支持的所有登录类型
     */
    @GetMapping
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(Collectors.toMap(
                oauth -> oauth.toLowerCase() + "登录",
                oauth -> SystemProperty.getHost() + "/oauth/login/" + oauth.toLowerCase()));
    }

    /**
     * 第三方登录
     */
    @RequestMapping("/login/{oauthType}")
    public void renderAuth(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return 登录成功后的信息
     */
    @RequestMapping("/{oauthType}/callback")
    public AuthResponse login(@PathVariable String oauthType, AuthCallback callback) {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse response = authRequest.login(callback);
        log.info("[response] = {}", JSONUtil.toJsonStr(response));
        return response;
    }

    private AuthSource getAuthSource(String type) {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }

    private ModelAndView buildFailResult(ModelAndView modelAndView){
        modelAndView.addObject(ResponseMsg.buildFailResult("认证失败"));
        modelAndView.setViewName("/result");
        return modelAndView;
    }

    /**
     * 账号密码登录认证
     */
    @PostMapping("/login/")
    public ModelAndView renderAuth(@RequestParam("email") String email, @RequestParam("passwd") String password,
                                   HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        // 认证邮箱
        User user = userService.getUser(email);
        if (null == user) {
            return buildFailResult(modelAndView);
        }
        // 认证密码
        AdminOAuth auth = adminOAuthService.getAdminOAuth(user.getUserId());
        if (null == auth){
            return buildFailResult(modelAndView);
        }
        String inputPassword = SecurityUtil.sha256Hex(PASSWORD_PREFIX + password + auth.getSalt());
        if(!inputPassword.equals(auth.getPassword())){
            return buildFailResult(modelAndView);
        }

        // cookie session
        modelAndView.setViewName("/index");
        return modelAndView;
    }
}
