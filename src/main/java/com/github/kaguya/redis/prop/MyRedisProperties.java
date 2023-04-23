package com.github.kaguya.redis.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * redis配置
 */
@Getter
@Setter
@ConfigurationProperties(MyRedisProperties.PREFIX)
@EnableConfigurationProperties(MyRedisProperties.class)
public class MyRedisProperties {

    /** 前缀 */
    public static final String PREFIX = "spring.redis";

    /** 是否开启Lettuce */
    private Boolean enable = true;
}
