package com.github.kaguya.constant;

import lombok.Getter;

public enum OAuthType {

    LOCAL_TYPE("LOCAL", "本地"),

    THIRD_TYPE("THIRD", "第三方");

    @Getter
    private String type;
    @Getter
    private String name;

    OAuthType(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
