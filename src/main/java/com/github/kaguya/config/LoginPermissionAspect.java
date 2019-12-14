package com.github.kaguya.config;

import com.github.kaguya.annotation.LoginPermission;
import com.github.kaguya.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Aspect 切面
 * @Order 注解排序
 */
@Aspect
@Component
@Order
@Slf4j
public class LoginPermissionAspect {

    /**
     * 切入点
     * Pointcut的定义包括两个部分：Pointcut表示式(expression)和Pointcut签名(signature)
     */
    /** Pointcut表示式 */
    @Pointcut("@annotation(com.github.kaguya.annotation.LoginPermission)")
    /** Pointcut签名 */
    public void signature(){}

    /**
     * 连接点
     * signature()对应上面的Pointcut签名
     * @param joinPoint 连接点，可以获得请求的相关信息
     * @param loginPermission 可以获得注解参数上的内容
     * @return
     * @throws Throwable
     */
    @Around("signature() && @annotation(loginPermission)")
    public Object doAround(ProceedingJoinPoint joinPoint, LoginPermission loginPermission) throws Throwable{
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes )RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        if (null == session.getAttribute(CommonConstant.SESSION_LOGIN_USER)){
            log.info("{} not login", session.getId());
            return "redirect:/loginPage";
        }
        return joinPoint.proceed();
    }

}
