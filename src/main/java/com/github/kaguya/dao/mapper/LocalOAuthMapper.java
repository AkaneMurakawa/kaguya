package com.github.kaguya.dao.mapper;

import com.github.kaguya.model.LocalOAuthUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocalOAuthMapper extends BaseMapper<LocalOAuthUser> {
}