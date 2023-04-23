package com.github.kaguya.biz.oauth.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocalUser extends OAuth implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 简介
     */
    private String description;
    /**
     * github
     */
    private String github;
    /**
     * twitter
     */
    private String twitter;
    /**
     * weibo
     */
    private String weibo;
}