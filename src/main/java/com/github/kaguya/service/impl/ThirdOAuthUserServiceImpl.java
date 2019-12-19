package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.ThirdOAuthUserMapper;
import com.github.kaguya.model.ThirdOAuthUser;
import com.github.kaguya.service.ThirdOAuthUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ThirdOAuthUserServiceImpl implements ThirdOAuthUserService {

    @Resource
    private ThirdOAuthUserMapper thirdOAuthUserMapper;

    @Override
    public int add(ThirdOAuthUser t) {
        return thirdOAuthUserMapper.insert(t);
    }

    @Override
    public ThirdOAuthUser select(ThirdOAuthUser t) {
        return thirdOAuthUserMapper.selectOne(t);
    }
}
