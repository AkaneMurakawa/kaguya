package com.github.kaguya.annotation;

import java.lang.annotation.*;

/**
 * 登录权限注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface LoginPermission {
}
