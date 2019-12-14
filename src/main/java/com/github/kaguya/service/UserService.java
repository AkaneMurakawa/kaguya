package com.github.kaguya.service;

import com.github.kaguya.model.User;

public interface UserService {

    User getUser(String user, String password);
}
