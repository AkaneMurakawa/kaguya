package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.AdminOAuthMapper;
import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.service.AdminOAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service

public class AdminOAuthServiceImpl implements AdminOAuthService {

    @Resource
    private AdminOAuthMapper adminOAuthMapper;

    @Override
    public AdminOAuth getAdminOAuth(Long userId) {
        AdminOAuth oAuth = new AdminOAuth();
        oAuth.setUserId(userId);
        return adminOAuthMapper.selectOne(oAuth);
    }
}
