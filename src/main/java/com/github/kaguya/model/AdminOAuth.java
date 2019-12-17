package com.github.kaguya.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminOAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long tid;
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
