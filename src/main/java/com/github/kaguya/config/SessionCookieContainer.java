package com.github.kaguya.config;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.model.User;
import com.github.kaguya.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

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
     * 获取登录用户信息
     *
     * @return
     */
    public User getLoginUser(HttpServletRequest request) {
        String userId = getUserId(request);
        return (User) redisDao.get(REDIS_EY_PREFIX + userId);
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
     * 设置SessionCookie-value
     * role:userId:过期时间:token(salt)
     * base64加密
     *
     * @return
     */
    public String setSessionCookieValue(AdminOAuth auth) {
        String cookieValue = new StringBuffer(128)
                .append(ROLE_ADMIN)
                .append(DELIMITER)
                .append(auth.getUserId())
                .append(DELIMITER)
                .append(COOKIE_EXPIRES_IN_MILLIS)
                .append(DELIMITER)
                .append(auth.getSalt())
                .toString();
        return SecurityUtil.base64(cookieValue.getBytes());
    }

    /**
     * 获取SessionCookie的值
     *
     * @param request
     */
    public String getSessionCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE.equals(cookie.getName())) {
                return SecurityUtil.base64Str(cookie.getValue());
            }
        }
        return "";
    }

    /**
     * 获取SessionCookie的值
     *
     * @param request
     */
    public String getUserId(HttpServletRequest request) {
        String value = getSessionCookieValue(request);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String userId = value.split(DELIMITER)[1];
        // TODO
        // 判断过期时间
        return userId;
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

    /**
     * 存储用户信息到redis
     *
     * @param user
     */
    private void saveUser(User user) {
        redisDao.set(REDIS_EY_PREFIX + user.getUserId(), user, REDIS_EXPIRE);
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    private void removeUser(String userId) {
        redisDao.del(REDIS_EY_PREFIX + userId);
    }

    /**
     * 存储用户信息到session
     *
     * @param request
     * @param response
     * @param key
     * @param value
     * @param expiry
     */
    private void setSession(HttpServletRequest request, HttpServletResponse response, String key, Object value, int expiry) {
        log.info("set session:{}", value);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(expiry);
        session.setAttribute(key, value);
    }

    /**
     * 清楚登录信息
     *
     * @param request
     * @param response
     */
    public void clear(HttpServletRequest request, HttpServletResponse response) {
        String userId = getUserId(request);
        removeUser(userId);
        Cookie cookie = new Cookie(SESSION_COOKIE, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
