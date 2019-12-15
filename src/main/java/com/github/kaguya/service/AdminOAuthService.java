package com.github.kaguya.service;

import com.github.kaguya.model.AdminOAuth;

public interface AdminOAuthService {

    AdminOAuth getAdminOAuth(Long userId);
}
