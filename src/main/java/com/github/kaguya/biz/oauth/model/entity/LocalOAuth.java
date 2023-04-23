package com.github.kaguya.biz.oauth.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocalOAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 密码hash
     */
    private String password;
}
