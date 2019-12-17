package com.github.kaguya.service.impl;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.CategoryMapper;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Category;
import com.github.kaguya.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static String KEY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "categories:";
    private final static long EXPIRE = 3600 * 24L;

    @Resource
    private RedisDao redisDao;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> listCategories() {
        String key = KEY_PREFIX + "list";
        List<Category> categoryList = (List<Category>) (List) redisDao.lGet(key, 0, -1);
        if (CollectionUtils.isEmpty(categoryList)) {
            categoryList = categoryMapper.selectAll();
            redisDao.lSet(key, (List<Object>) (List) categoryList, EXPIRE);
        }
        return categoryList;
    }

    @Override
    public ResponseMsg add(String name) {
        Category category = new Category();
        int result = categoryMapper.insert(category);
        if (result > 0) {
            return ResponseMsg.buildSuccessResult();
        } else {
            return ResponseMsg.buildFailResult();
        }
    }
}
