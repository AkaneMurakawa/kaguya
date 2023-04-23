package com.github.kaguya.biz.category.service;

import com.github.kaguya.constant.RedisKeyPrefix;

public interface ICategoryKey {

    String KEY_PREFIX = RedisKeyPrefix.KAGUYA_WEB + "categories:";

    String KEY_LIST = KEY_PREFIX + "list";
}
