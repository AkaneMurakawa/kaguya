package com.github.kaguya.service;

import com.github.kaguya.model.LocalUser;

public interface LocalUserService {

    LocalUser getUser(String email);

}
