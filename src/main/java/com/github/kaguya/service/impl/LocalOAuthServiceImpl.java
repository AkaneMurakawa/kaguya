package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.LocalOAuthMapper;
import com.github.kaguya.model.LocalOAuthUser;
import com.github.kaguya.service.LocalOAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service

public class LocalOAuthServiceImpl implements LocalOAuthService {

    @Resource
    private LocalOAuthMapper localOAuthMapper;

    @Override
    public LocalOAuthUser getLocalOAuth(Long userId) {
        LocalOAuthUser oAuth = new LocalOAuthUser();
        oAuth.setUserId(userId);
        return localOAuthMapper.selectOne(oAuth);
    }
}
