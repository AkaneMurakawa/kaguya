package com.github.kaguya.biz.oauth.service;

import com.github.kaguya.biz.oauth.model.entity.LocalOAuth;

public interface LocalOAuthService {

    /**
     * 获取用户授权信息
     */
    LocalOAuth getLocalOAuth(Long userId);
}
