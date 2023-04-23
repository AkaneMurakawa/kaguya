package com.github.kaguya.biz.category.service;

import com.github.kaguya.biz.category.model.entity.Category;
import java.util.List;

public interface CategoryService {

    /**
     * 查询所有分类
     */
    List<Category> listCategories();

    /**
     * 新增分类
     */
    void add(String name);
}
