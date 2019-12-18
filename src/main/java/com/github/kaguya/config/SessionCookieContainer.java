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
    private static final String REDIS_EY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "user:";
    private static final long REDIS_EXPIRE = 3600 * 24L;
    private static final int SESSION_EXPIRES_IN_SECONDS = 3600;
    private static final int COOKIE_EXPIRES_IN_SECONDS = 3600 * 24 * 7;
    private static final long COOKIE_EXPIRES_IN_MILLIS = COOKIE_EXPIRES_IN_SECONDS * 1000L;
    private static final String DELIMITER = ":";

    @Resource
    private RedisDao redisDao;

    /**
     * 是否登录，依赖cookie
     *
     * @return true 是， false 否
     */
    public boolean isLogin(HttpServletRequest request) {
        if (null == getUserId(request)) {
            return false;
        }
        return true;
    }

    /**
     * 获取登录用户信息
     */
    public User getLoginUser(HttpServletRequest request) {
        String userId = getUserId(request);
        if (null == userId) {
            return null;
        }
        User user = null;
        if (null == request.getSession().getAttribute(USER_SESSION)) {
            user = (User) redisDao.get(REDIS_EY_PREFIX + userId);
            setSession(request, USER_SESSION, user, 0);
        }
        return user;
    }

    /**
     * 认证密码
     * prefix + password + token
     *
     * @param password 密码
     * @param token    盐值
     */
    public String getPassword(String password, String token) {
        return SecurityUtil.sha256Hex(PASSWORD_PREFIX + password + token);
    }

    /**
     * 设置SessionCookie-value
     * role:userId:过期时间:token(salt)
     * base64加密
     */
    public String setSessionCookieValue(AdminOAuth auth) {
        return setSessionCookieValue(auth.getUserId(), auth.getSalt());
    }

    /**
     * 设置SessionCookie-value
     * role:userId:过期时间:token(salt)
     * base64加密
     */
    public String setSessionCookieValue(Long userId, String token) {
        String cookieValue = new StringBuffer(128)
                .append(ROLE_ADMIN)
                .append(DELIMITER)
                .append(userId)
                .append(DELIMITER)
                .append(System.currentTimeMillis() + COOKIE_EXPIRES_IN_MILLIS)
                .append(DELIMITER)
                .append(token)
                .toString();
        return SecurityUtil.base64(cookieValue.getBytes());
    }

    /**
     * 设置SessionCookie
     *
     * @param value  cookie值
     * @param expiry 过期时间，单位秒。0表示使用默认
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
     */
    public String getSessionCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE.equals(cookie.getName())) {
                return SecurityUtil.base64Str(cookie.getValue());
            }
        }
        return null;
    }

    /**
     * 获取userId，如果过期返回null
     */
    public String getUserId(HttpServletRequest request) {
        String value = getSessionCookieValue(request);
        if (null == value) {
            return null;
        }
        // 判断是否过期
        Long expire = Long.parseLong(value.split(DELIMITER)[2]);
        if (System.currentTimeMillis() < expire) {
            return null;
        }
        return value.split(DELIMITER)[1];
    }

    /**
     * 设置session
     */
    public void setSession(HttpServletRequest request) {
        getLoginUser(request);
    }

    /**
     * 设置session
     *
     * @param expiry 过期时间，单位秒。0表示使用默认
     */
    public void setSession(HttpServletRequest request, User user, int expiry) {
        saveUser(user);

        User sessionUser = new User();
        sessionUser.setUsername(user.getUsername());
        sessionUser.setAvatar(SecurityUtil.base64Str(user.getAvatar()));
        setSession(request, USER_SESSION, sessionUser, expiry);
    }

    /**
     * 存储用户信息到session
     */
    private void setSession(HttpServletRequest request, String key, Object value, int expiry) {
        log.info("set session:{}", value);
        if (0 == expiry) {
            expiry = SESSION_EXPIRES_IN_SECONDS;
        }
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(expiry);
        session.setAttribute(key, value);
    }

    /**
     * 获取用户session
     */
    public Object getUserSession(HttpServletRequest request) {
        return request.getSession().getAttribute(USER_SESSION);
    }

    /**
     * 存储用户信息到redis
     */
    private void saveUser(User user) {
        redisDao.set(REDIS_EY_PREFIX + user.getUserId(), user, REDIS_EXPIRE);
    }

    /**
     * 删除用户
     */
    private void removeUser(String userId) {
        redisDao.del(REDIS_EY_PREFIX + userId);
    }

    /**
     * 清除登录信息
     */
    public void clear(HttpServletRequest request, HttpServletResponse response) {
        String userId = getUserId(request);
        if (userId != null) {
            removeUser(userId);
            Cookie cookie = new Cookie(SESSION_COOKIE, null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        request.getSession().removeAttribute(USER_SESSION);
    }
}
