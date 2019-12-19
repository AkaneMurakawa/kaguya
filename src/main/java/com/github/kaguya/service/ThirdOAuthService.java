package com.github.kaguya.service;

import com.github.kaguya.model.ThirdOAuth;

public interface ThirdOAuthService {

    int add(ThirdOAuth t);

    ThirdOAuth select(ThirdOAuth t);
}
