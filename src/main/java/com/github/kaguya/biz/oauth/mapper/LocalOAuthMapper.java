package com.github.kaguya.biz.oauth.mapper;

import com.github.kaguya.mapper.BaseMapper;
import com.github.kaguya.biz.oauth.model.entity.LocalOAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocalOAuthMapper extends BaseMapper<LocalOAuth> {
}