package com.github.kaguya;

import com.github.kaguya.dao.mapper.AdminOAuthMapper;
import com.github.kaguya.dao.mapper.CategoryMapper;
import com.github.kaguya.dao.mapper.UserMapper;
import com.github.kaguya.model.AdminOAuth;
import com.github.kaguya.model.Category;
import com.github.kaguya.model.User;
import com.github.kaguya.util.SecurityUtil;
import com.github.kaguya.util.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class KaguyaApplicationTests {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AdminOAuthMapper adminOAuthMapper;

    @Test
    public void category(){
        Category category0 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("主页")
                .setOrderId(0);
        categoryMapper.insert(category0);

        Category category1 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("Java")
                .setOrderId(1);
        categoryMapper.insert(category1);

        Category category2 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("设计模式")
                .setOrderId(2);
        categoryMapper.insert(category2);

        Category category3 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("Spring")
                .setOrderId(3);
        categoryMapper.insert(category3);

        Category category4 = new Category()
                .setCategoryId(SnowFlake.generateId())
                .setName("数据结构")
                .setOrderId(4);
        categoryMapper.insert(category4);
    }

    @Test
    public void user(){
        Long userId = SnowFlake.generateId();
        User user = new User();
        user.setUserId(userId);
        user.setUsername("AkaneMurakwa");
        user.setEmail("chenshjing@gmail.com");
        userMapper.insert(user);

        AdminOAuth adminOAuth = new AdminOAuth();
        adminOAuth.setUserId(userId);
        adminOAuth.setSalt(SecurityUtil.sha256Hex(userId.toString()));
        adminOAuth.setPassword(SecurityUtil.sha256Hex("123456"));
        adminOAuthMapper.insert(adminOAuth);
    }

}
