package com.github.kaguya.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ThirdOAuthUser extends OAuthUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    protected Long userId;
    /**
     * 昵称
     */
    protected String username;
    /**
     * 邮箱(登录账号)
     */
    protected String email;
    /**
     * 头像
     */
    protected String avatar;
    /**
     * token
     */
    private String token;
    /**
     * 别名
     */
    private String nickname;
    /**
     * 博客
     */
    private String blog;
    /**
     * 公司
     */
    private String company;
    /**
     * 位置
     */
    private String location;
    /**
     * 备注
     */
    private String remark;

}
