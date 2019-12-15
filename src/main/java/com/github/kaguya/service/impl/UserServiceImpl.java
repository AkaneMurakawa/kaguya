package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.UserMapper;
import com.github.kaguya.model.User;
import com.github.kaguya.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUser(String email) {
        User one = new User().setEmail(email);
        return userMapper.selectOne(one);
    }
}
