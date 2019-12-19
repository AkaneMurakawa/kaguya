package com.github.kaguya.service;

import com.github.kaguya.model.LocalOAuth;

public interface LocalOAuthService {

    LocalOAuth getLocalOAuth(Long userId);
}
