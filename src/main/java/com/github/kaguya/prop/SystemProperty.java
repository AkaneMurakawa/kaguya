package com.github.kaguya.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置
 */
@Data
@ConfigurationProperties("kaguya.system.config")
public class SystemProperty {

    private String author;

    private String email;

    private String host;

}
