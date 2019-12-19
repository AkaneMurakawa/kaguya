package com.github.kaguya.service;

import com.github.kaguya.model.LocalOAuthUser;

public interface LocalOAuthService {

    LocalOAuthUser getLocalOAuth(Long userId);
}
