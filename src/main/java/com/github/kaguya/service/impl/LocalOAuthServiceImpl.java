package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.LocalOAuthMapper;
import com.github.kaguya.model.LocalOAuth;
import com.github.kaguya.service.LocalOAuthService;
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
