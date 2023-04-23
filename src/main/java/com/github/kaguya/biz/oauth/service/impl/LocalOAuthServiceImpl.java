package com.github.kaguya.biz.oauth.service.impl;

import com.github.kaguya.biz.oauth.mapper.LocalOAuthMapper;
import com.github.kaguya.biz.oauth.model.entity.LocalOAuth;
import com.github.kaguya.biz.oauth.service.LocalOAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service

public class LocalOAuthServiceImpl implements LocalOAuthService {

    @Resource
    private LocalOAuthMapper localOAuthMapper;

    @Override
    public LocalOAuth getLocalOAuth(Long userId) {
        LocalOAuth oAuth = new LocalOAuth();
        oAuth.setUserId(userId);
        return localOAuthMapper.selectOne(oAuth);
    }
}
