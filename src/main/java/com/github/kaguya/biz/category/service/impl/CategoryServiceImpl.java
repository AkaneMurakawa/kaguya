package com.github.kaguya.biz.category.service.impl;

import com.github.kaguya.biz.category.mapper.CategoryMapper;
import com.github.kaguya.biz.category.service.ICategoryKey;
import com.github.kaguya.config.redis.client.RedisClient;
import com.github.kaguya.biz.category.model.entity.Category;
import com.github.kaguya.biz.category.service.CategoryService;
import com.github.kaguya.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService, ICategoryKey {

    private static final long EXPIRE = 3600 * 24L;

    @Resource
    private RedisClient redisClient;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> listCategories() {
        List<Category> categoryList = (List<Category>) (List) redisClient.lGet(KEY_LIST, 0, -1);
        if (CollectionUtils.isEmpty(categoryList)) {
            categoryList = categoryMapper.selectAll();
            // 存储到redis，这里需要类型转换
            if (categoryList != null && categoryList.size() > 0) {
                redisClient.lSet(KEY_LIST, (List<Object>) (List) categoryList, EXPIRE);
            }
        }
        return categoryList;
    }

    @Override
    public void add(String name) {
        Category category = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName(name)
                .setOrderId(getNextOrderId());
        int result = categoryMapper.insert(category);
        if (result > 0) {
            // 清除redis缓存
            redisClient.del(KEY_LIST);
        }
    }

    /**
     * 获取下一个orderId
     */
    public Integer getNextOrderId() {
        // 获取最大的orderId
        Integer orderId = categoryMapper.getMaxOrderIdBy();
        return null == orderId ? 0 : orderId + 1;
    }
}
