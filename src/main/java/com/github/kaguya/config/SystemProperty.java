package com.github.kaguya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 */
@Component
public class SystemProperty {

    private static String author;

    public static String getAuthor() {
        return author;
    }

    @Value("${system.property.author:AkaneMurakawa}")
    public void setAuthor(String author) {
        SystemProperty.author = author;
    }

    private static String host;

    public static String getHost() {
        return host;
    }

    @Value("${system.property.host:localhost:9420}")
    public void setHost(String host) {
        SystemProperty.host = host;
    }

    private static String email;

    public static String getEmail() {
        return email;
    }

    @Value("${system.property.email}")
    public void setEmail(String email) {
        SystemProperty.email = email;
    }
}
