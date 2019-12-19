package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.LocalUserMapper;
import com.github.kaguya.model.LocalUser;
import com.github.kaguya.service.LocalUserService;
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
