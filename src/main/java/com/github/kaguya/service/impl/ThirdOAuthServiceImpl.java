package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.ThirdOAuthMapper;
import com.github.kaguya.model.ThirdOAuth;
import com.github.kaguya.service.ThirdOAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ThirdOAuthServiceImpl implements ThirdOAuthService {

    @Resource
    private ThirdOAuthMapper thirdOAuthMapper;

    @Override
    public int add(ThirdOAuth t) {
        return thirdOAuthMapper.insert(t);
    }

    @Override
    public ThirdOAuth select(ThirdOAuth t) {
        return thirdOAuthMapper.selectOne(t);
    }
}
