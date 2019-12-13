package com.github.kaguya;

import com.github.kaguya.dao.mapper.CategoryMapper;
import com.github.kaguya.model.Category;
import com.github.kaguya.util.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CategoryMapperTest {

    @Resource
    private CategoryMapper categoryMapper;

    @Test
    public void add(){
        Category category1 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("Java")
                .setOrderId(0);
        categoryMapper.insert(category1);

        Category category2 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("设计模式")
                .setOrderId(1);
        categoryMapper.insert(category2);

        Category category3 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("Spring")
                .setOrderId(2);
        categoryMapper.insert(category3);

        Category category4 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("数据结构")
                .setOrderId(3);
        categoryMapper.insert(category4);
    }
}
