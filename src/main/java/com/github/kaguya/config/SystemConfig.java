package com.github.kaguya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 系统配置类
 */
@Configuration
public class SystemConfig {

    @Bean("localeResolver")
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }
}
