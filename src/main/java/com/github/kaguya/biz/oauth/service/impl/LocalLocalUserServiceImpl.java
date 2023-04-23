package com.github.kaguya.biz.oauth.service.impl;

import com.github.kaguya.biz.oauth.mapper.LocalUserMapper;
import com.github.kaguya.biz.oauth.model.entity.LocalUser;
import com.github.kaguya.biz.oauth.service.LocalUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class LocalLocalUserServiceImpl implements LocalUserService {

    @Resource
    private LocalUserMapper localUserMapper;

    @Override
    public LocalUser getUser(String email) {
        LocalUser one = new LocalUser();
        one.setEmail(email);
        return localUserMapper.selectOne(one);
    }
}
