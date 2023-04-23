package com.github.kaguya.biz.document.service;

import com.github.kaguya.constant.RedisKeyPrefix;

public interface IDocumentKey {

    String KEY_PREFIX = RedisKeyPrefix.KAGUYA_WEB + "docs:";

    String KEY_CONTENT = KEY_PREFIX + "content:";

    String KEY_HTML = KEY_PREFIX + "html:";

    String KEY_TREE = KEY_PREFIX + "tree:";

}
