package com.github.kaguya.biz.oauth.service;

import com.github.kaguya.biz.oauth.model.entity.ThirdOAuth;

public interface ThirdOAuthService {

    /**
     * 添加第三方用户授权信息
     */
    int add(ThirdOAuth t);
}
