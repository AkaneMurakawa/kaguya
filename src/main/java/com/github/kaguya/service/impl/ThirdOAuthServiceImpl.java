package com.github.kaguya.service.impl;

import com.github.kaguya.dao.mapper.ThirdOAuthMapper;
import com.github.kaguya.model.ThirdOAuth;
import com.github.kaguya.service.ThirdOAuthService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

@Service
public class ThirdOAuthServiceImpl implements ThirdOAuthService {

    @Resource
    private ThirdOAuthMapper thirdOAuthMapper;

    @Override
    public int add(ThirdOAuth t) {
        ThirdOAuth thirdOAuth = new ThirdOAuth();
        thirdOAuth.setUserId(t.getUserId());
        // 如果存在，则直接更新信息
        if (thirdOAuthMapper.selectOne(thirdOAuth) != null){
            Example example = new Example(ThirdOAuth.class);
            example.createCriteria().andEqualTo("user_id", t.getUserId());
            return thirdOAuthMapper.updateByExample(t, example);
        }
        return thirdOAuthMapper.insert(t);
    }

    @Override
    public ThirdOAuth select(ThirdOAuth t) {
        return thirdOAuthMapper.selectOne(t);
    }
}
