package com.github.kaguya.biz.oauth.service;

import com.github.kaguya.biz.oauth.model.entity.LocalUser;

public interface LocalUserService {

    /**
     * 获取用户
     */
    LocalUser getUser(String email);

}
