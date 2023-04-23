package com.github.kaguya.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型
 */
@Getter
@AllArgsConstructor
public enum OAuthType {

    LOCAL_TYPE("LOCAL", "本地"),

    THIRD_TYPE("THIRD", "第三方");

    private String code;

    private String desc;

}
