package com.github.kaguya.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system.config")
public class SystemConfiguration {

    private String name;
}
