package com.github.kaguya.biz.document.mapper;

import com.github.kaguya.base.mapper.BaseMapper;
import com.github.kaguya.biz.document.model.entity.Document;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocumentMapper extends BaseMapper<Document> {
}
