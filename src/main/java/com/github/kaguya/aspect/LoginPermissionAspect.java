package com.github.kaguya.aspect;

import com.github.kaguya.config.LoginContainer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录注解处理器
 */
@Aspect
@Slf4j
@Component
public class LoginPermissionAspect {

    @Resource
    private LoginContainer loginContainer;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.github.kaguya.annotation.LoginPermission)")
    public void permissionPointcut() {
    }

    /**
     * 连接点
     */
    @Around("permissionPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = loginContainer.getRequest();
        if (!loginContainer.isLogin()) {
            log.warn("{} not login", request.getRemoteHost());
            return "redirect:/loginPage";
        }
        return joinPoint.proceed();
    }

}
