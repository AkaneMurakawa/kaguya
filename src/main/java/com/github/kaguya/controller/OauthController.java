package com.github.kaguya.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.kaguya.config.SessionCookieContainer;
import com.github.kaguya.config.SystemProperty;
import com.github.kaguya.constant.OAuthType;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.LocalOAuthUser;
import com.github.kaguya.model.ThirdOAuthUser;
import com.github.kaguya.model.User;
import com.github.kaguya.service.LocalOAuthService;
import com.github.kaguya.service.ThirdOAuthUserService;
import com.github.kaguya.service.UserService;
import com.github.kaguya.util.SecurityUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.BeanUtils;
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

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {

    private final AuthRequestFactory factory;
    private static final String PREFIX = "/oauth";
    private static final int SUCCESS_CODE = 2000;

    @Resource
    private UserService userService;
    @Resource
    private ThirdOAuthUserService thirdOAuthUserService;
    @Resource
    private LocalOAuthService localOAuthService;
    @Resource
    private SessionCookieContainer sessionCookieContainer;

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
    @RequestMapping(PREFIX + "/login/{oauthType}")
    public void renderAuth(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     */
    @RequestMapping("/")
    public ModelAndView login(AuthCallback callback, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        // 未登录时的跳转
        if (null == callback.getAuth_code()){
            modelAndView.setViewName("/index");
            return modelAndView;
        }
        // TODO authType
        AuthRequest authRequest = factory.get(getAuthSource("GITHUB"));
        AuthResponse authResponse = authRequest.login(callback);
        log.info("[response] = {}", JSONUtil.toJsonStr(response));

        // 获取第三方返回登录成功的信息
        AuthUser user = (AuthUser) authResponse.getData();
        String email = user.getEmail();
        if (SUCCESS_CODE != authResponse.getCode() || !SystemProperty.getEmail().equals(email)){
            return buildFailResult(modelAndView);
        }

        // 第三方数据
        String username = user.getUsername();
        String avatar = user.getAvatar();
        Long userId = Long.parseLong(user.getUuid());
        String token = user.getToken().getAccessToken();

        ThirdOAuthUser sessionUser = new ThirdOAuthUser();
        BeanUtils.copyProperties(user, sessionUser);
        sessionUser.setAvatar(SecurityUtil.base64(avatar.getBytes()));

        // 存储第三方登录用户信息
        thirdOAuthUserService.add(sessionUser);
        // session cookie
        String cookieValue = sessionCookieContainer.setSessionCookieValue(OAuthType.THIRD_TYPE, userId, token);
        sessionCookieContainer.setSessionCookie(request, response, cookieValue, 0);
        sessionCookieContainer.setSession(request, sessionUser, 0);
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    /**
     * 验证登录类型
     */
    private AuthSource getAuthSource(String type) {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }

    /**
     * 认证失败处理
     */
    private ModelAndView buildFailResult(ModelAndView modelAndView) {
        modelAndView.addObject("result", ResponseMsg.buildFailResult("认证失败"));
        modelAndView.setViewName("/result");
        return modelAndView;
    }

    /**
     * 账号密码登录认证
     */
    @PostMapping(PREFIX + "/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password,
                                   HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        // 认证邮箱
        User user = userService.getUser(email);
        if (null == user) {
            return buildFailResult(modelAndView);
        }
        // 认证密码
        LocalOAuthUser auth = localOAuthService.getLocalOAuth(user.getUserId());
        if (null == auth) {
            return buildFailResult(modelAndView);
        }
        String inputPassword = sessionCookieContainer.getPassword(password, auth.getSalt());
        if (!inputPassword.equals(auth.getPassword())) {
            return buildFailResult(modelAndView);
        }

        // session cookie
        String cookieValue = sessionCookieContainer.setSessionCookieValue(auth);
        sessionCookieContainer.setSessionCookie(request, response, cookieValue, 0);
        sessionCookieContainer.setSession(request, user, 0);
        modelAndView.setViewName("/index");
        return modelAndView;
    }

}
