package com.github.kaguya.config;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.model.User;
import com.github.kaguya.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登陆控制容器
 */
@Slf4j
@Component
public class SessionCookieContainer {

    private static final String SESSION_COOKIE = "_session_";
    private static final String USER_SESSION = "user";
    private static final String ROLE_ADMIN = "admin";
    private static final String PASSWORD_PREFIX = "kaguya";
    private final static String REDIS_EY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "user:";
    private final static long REDIS_EXPIRE = 3600 * 24L;
    private static final int SESSION_EXPIRES_IN_SECONDS = 3600;
    private static final int COOKIE_EXPIRES_IN_SECONDS = 3600 * 24 * 7;
    private static final long COOKIE_EXPIRES_IN_MILLIS = COOKIE_EXPIRES_IN_SECONDS * 1000L;

    @Resource
    private RedisDao redisDao;

    public static String getSessionCookie() {
        return SESSION_COOKIE;
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = null;

        return user;
    }

    /**
     * 认证密码
     * prefix + password + token
     *
     * @param password
     * @param token
     * @return
     */
    public String getPassword(String password, String token) {
        return SecurityUtil.sha256Hex(PASSWORD_PREFIX + password + token);
    }

    /**
     * 设置SessionCookie-value
     * role:userId:过期时间:token(salt)
     * base64加密
     *
     * @return
     */
    public String setSessionCookieValue(AdminOAuth auth) {
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
     *
     * @param value    cookie值
     * @param expiry   过期时间，单位秒。0表示使用默认
     * @param request
     * @param response
     */
    public void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String value, int expiry) {
        log.info("set session cookie:{}", value);
        if (0 == expiry) {
            expiry = COOKIE_EXPIRES_IN_SECONDS;
        }
        Cookie cookie = new Cookie(SESSION_COOKIE, value);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }

    /**
     * 获取SessionCookie的值
     *
     * @param request
     * @param response
     */
    public String getSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE.equals(cookie.getName())) {
                return SecurityUtil.base64Str(cookie.getValue());
            }
        }
        return "";
    }

    /**
     * 设置Cookie
     *
     * @param expiry   过期时间，单位秒。0表示使用默认
     * @param request
     * @param response
     */
    public void setSession(HttpServletRequest request, HttpServletResponse response, User user, int expiry) {
        saveUser(user);

        if (0 == expiry) {
            expiry = SESSION_EXPIRES_IN_SECONDS;
        }
        User sessionUser = new User();
        sessionUser.setUsername(user.getUsername());
        sessionUser.setAvatar(SecurityUtil.base64Str(user.getAvatar()));
        setSession(request, response, USER_SESSION, sessionUser, expiry);
    }

    private void saveUser(User user){
        redisDao.set(REDIS_EY_PREFIX + user.getUserId(), user, REDIS_EXPIRE);
    }

    private void setSession(HttpServletRequest request, HttpServletResponse response, String key, Object value, int expiry) {
        log.info("set session:{}", value);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(expiry);
        session.setAttribute(key, value);
    }
}
