package com.github.kaguya.config;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.constant.OAuthType;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.ThirdOAuthMapper;
import com.github.kaguya.dao.mapper.LocalUserMapper;
import com.github.kaguya.model.LocalOAuth;
import com.github.kaguya.model.LocalUser;
import com.github.kaguya.model.OAuth;
import com.github.kaguya.model.ThirdOAuth;
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
    private static final String PASSWORD_PREFIX = "kaguya";
    private static final String REDIS_EY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "user:";
    private static final long REDIS_EXPIRE = 3600 * 24L;
    private static final int SESSION_EXPIRES_IN_SECONDS = 3600;
    private static final int COOKIE_EXPIRES_IN_SECONDS = 3600 * 24 * 7;
    private static final long COOKIE_EXPIRES_IN_MILLIS = COOKIE_EXPIRES_IN_SECONDS * 1000L;
    private static final String DELIMITER = ":";

    @Resource
    private RedisDao redisDao;
    @Resource
    private LocalUserMapper localUserMapper;
    @Resource
    private ThirdOAuthMapper thirdOAuthMapper;

    /**
     * 是否登录，依赖cookie
     *
     * @return true 是， false 否
     */
    public boolean isLogin(HttpServletRequest request) {
        if (null == getUserTypeAndId(request)) {
            return false;
        }
        return true;
    }

    /**
     * 获取登录用户信息
     */
    public OAuth getLoginUser(HttpServletRequest request) {
        String[] userTypeAndId = getUserTypeAndId(request);
        if (null == userTypeAndId) {
            return null;
        }

        OAuth user = null;
        String userType = userTypeAndId[0];
        String userId = userTypeAndId[1];
        // 本地登录
        if (OAuthType.LOCAL_TYPE.getType().equals(userType)) {
            user = (LocalUser) redisDao.get(REDIS_EY_PREFIX + userId);
            if (null == user){
                LocalUser tempLocalUserUser = new LocalUser();
                tempLocalUserUser.setUserId(Long.parseLong(userId));
                user = localUserMapper.selectOne(tempLocalUserUser);
            }
        } else {
            // 第三方登录
            user = (ThirdOAuth) redisDao.get(REDIS_EY_PREFIX + userId);
            if (null == user){
                ThirdOAuth tempUser = new ThirdOAuth();
                tempUser.setUserId(Long.parseLong(userId));
                user = thirdOAuthMapper.selectOne(tempUser);
            }
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
     * type:userId:过期时间:token(salt)
     * base64加密
     */
    public String setSessionCookieValue(LocalOAuth auth) {
        return setSessionCookieValue(OAuthType.LOCAL_TYPE, auth.getUserId(), auth.getSalt());
    }

    /**
     * 设置SessionCookie-value
     * type:userId:过期时间:token(salt)
     * base64加密
     */
    public String setSessionCookieValue(OAuthType oAuthType, Long userId, String token) {
        String cookieValue = new StringBuffer(128)
                .append(oAuthType.getType())
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
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE.equals(cookie.getName())) {
                    return SecurityUtil.base64Str(cookie.getValue());
                }
            }
        }
        return null;
    }

    /**
     * 获取userType和userId，如果过期返回null
     * String[0] Type
     * String[1] Id
     */
    public String[] getUserTypeAndId(HttpServletRequest request) {
        String value = getSessionCookieValue(request);
        if (null == value) {
            return null;
        }
        // 判断是否过期
        Long expire = Long.parseLong(value.split(DELIMITER)[2]);
        if (System.currentTimeMillis() < expire) {
            return null;
        }
        String[] user = new String[2];
        user[0] = value.split(DELIMITER)[0];
        user[1] = value.split(DELIMITER)[1];
        return user;
    }

    /**
     * 设置session，只存储username和avatar
     *
     * @param expiry 过期时间，单位秒。0表示使用默认
     */
    public void setSession(HttpServletRequest request, OAuth user, int expiry) {
        saveUser(user);

        OAuth sessionUser = new OAuth();
        sessionUser.setUsername(user.getUsername());
        sessionUser.setAvatar(SecurityUtil.base64Str(user.getAvatar()));
        setSession(request, USER_SESSION, sessionUser, expiry);
    }

    /**
     * 设置session，只存储username和avatar
     */
    public void setSession(HttpServletRequest request, OAuth user) {
        OAuth sessionUser = new OAuth();
        sessionUser.setUsername(user.getUsername());
        sessionUser.setAvatar(SecurityUtil.base64Str(user.getAvatar()));
        setSession(request, USER_SESSION, sessionUser, 0);
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
    private void saveUser(OAuth user) {
        redisDao.set(REDIS_EY_PREFIX + user.getUserId(), user, REDIS_EXPIRE * 7);
    }

    /**
     * 删除用户
     */
    private void removeUser(String userId) {
        redisDao.del(REDIS_EY_PREFIX + userId);
    }

    /**
     * 清除登录信息
     * cookie
     * session
     * redis
     */
    public void clear(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_COOKIE, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        request.getSession().removeAttribute(USER_SESSION);

        String[] userTypeAndId = getUserTypeAndId(request);
        if (userTypeAndId != null) {
            String userId = userTypeAndId[1];
            removeUser(userId);
        }
    }
}
