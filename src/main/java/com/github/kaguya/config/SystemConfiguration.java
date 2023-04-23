package com.github.kaguya.config;

import com.github.kaguya.prop.SystemProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 系统配置类
 */
@Configuration
@EnableConfigurationProperties(SystemProperty.class)
public class SystemConfiguration {

    @Bean("localeResolver")
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }
}
