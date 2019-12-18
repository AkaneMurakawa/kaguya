package com.github.kaguya.dao.mapper;

import com.github.kaguya.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获得分最大的orderId
     */
    Integer getMaxOrderIdBy();
}
