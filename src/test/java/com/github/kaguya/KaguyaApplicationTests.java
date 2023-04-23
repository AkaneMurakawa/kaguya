package com.github.kaguya;

import com.github.kaguya.config.LoginContainer;
import com.github.kaguya.biz.category.mapper.CategoryMapper;
import com.github.kaguya.biz.oauth.mapper.LocalOAuthMapper;
import com.github.kaguya.biz.oauth.mapper.LocalUserMapper;
import com.github.kaguya.biz.category.model.entity.Category;
import com.github.kaguya.biz.oauth.model.entity.LocalOAuth;
import com.github.kaguya.biz.oauth.model.entity.LocalUser;
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
    private LocalUserMapper localUserMapper;
    @Resource
    private LocalOAuthMapper localOAuthMapper;
    @Resource
    private LoginContainer loginContainer;

    @Test
    public void category() {
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
    public void user() {
        Long userId = SnowFlake.generateId();
        LocalUser localUser = new LocalUser();
        localUser.setUserId(userId);
        localUser.setUsername("kaguya");
        localUser.setEmail("test@gmail.com");
        localUser.setAvatar(SecurityUtil.base64("https://avatars1.githubusercontent.com/u/23401691?s=460&v=4".getBytes()));
        localUserMapper.insert(localUser);

        LocalOAuth localOAuth = new LocalOAuth();
        localOAuth.setUserId(userId);
        localOAuth.setSalt(SecurityUtil.sha256Hex(userId.toString()));
        localOAuth.setPassword(loginContainer.getPassword("123456", localOAuth.getSalt()));
        localOAuthMapper.insert(localOAuth);
    }

    @Test
    public void base64() {
        String encode = SecurityUtil.base64("https://avatars1.githubusercontent.com/u/23401691?s=460&v=4".getBytes());
        byte[] decode = SecurityUtil.base64(encode);
        System.out.println(encode);
        System.out.println(new String(decode));

    }
}
