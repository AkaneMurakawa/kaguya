package com.github.kaguya.service;

import com.github.kaguya.model.ThirdOAuthUser;

public interface ThirdOAuthUserService {

    int add(ThirdOAuthUser t);

    ThirdOAuthUser select(ThirdOAuthUser t);
}
