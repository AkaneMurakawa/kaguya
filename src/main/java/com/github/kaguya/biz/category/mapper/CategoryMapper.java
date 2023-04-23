package com.github.kaguya.biz.category.mapper;

import com.github.kaguya.biz.category.model.entity.Category;
import com.github.kaguya.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获得分最大的orderId
     */
    Integer getMaxOrderIdBy();
}
