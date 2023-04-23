package com.github.kaguya.biz.oauth.mapper;

import com.github.kaguya.mapper.BaseMapper;
import com.github.kaguya.biz.oauth.model.entity.LocalUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LocalUserMapper extends BaseMapper<LocalUser> {

}