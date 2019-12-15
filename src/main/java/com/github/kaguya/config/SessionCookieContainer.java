package com.github.kaguya.config;

import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.model.User;
import com.github.kaguya.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆控制容器
 */
@Slf4j
public class SessionCookieContainer {

    private static final String SESSION_COOKIE = "_session_";
    private static final String ROLE_ADMIN = "admin";
    private static final String PASSWORD_PREFIX = "kaguya";
    private static final int COOKIE_EXPIRES_IN_SECONDS = 3600 * 24 * 7;
    private static final long COOKIE_EXPIRES_IN_MILLIS = COOKIE_EXPIRES_IN_SECONDS * 1000L;

    public static String getSessionCookie() {
        return SESSION_COOKIE;
    }

    /**
     * 获取登录用户信息
     * @return
     */
    public static User getLoginUser(){
        User user = null;

        return user;
    }

    /**
     * 认证密码
     * prefix + password + token
     * @param password
     * @param token
     * @return
     */
    public static String getPassword(String password, String token){
        return SecurityUtil.sha256Hex(PASSWORD_PREFIX + password + token);
    }

    /**
     * 设置SessionCookie-value
     * role:userId:过期时间:token(salt)
     * base64加密
     * @return
     */
    public static String setCookieValue(AdminOAuth auth){
        String cookieValue = new StringBuffer(128)
                .append(ROLE_ADMIN)
                .append(":")
                .append(auth.getUserId())
                .append(":")
                .append(COOKIE_EXPIRES_IN_MILLIS)
                .append(":")
                .append(auth.getSalt())
                .toString();
        return SecurityUtil.base64(cookieValue.getBytes());
    }

    /**
     * 设置SessionCookie
     * @param value cookie值
     * @param expiry 过期时间，单位秒。0表示使用默认
     * @param request
     * @param response
     */
    public static void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String value, int expiry) {
        log.info("set session cookie:{}", value);
        Cookie cookie = new Cookie(SESSION_COOKIE, value);
        if (0 == expiry){
            expiry = COOKIE_EXPIRES_IN_SECONDS;
        }
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }
}
