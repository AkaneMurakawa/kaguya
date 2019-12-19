package com.github.kaguya.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
public class ThirdOAuth extends OAuth implements Serializable {
    private static final long serialVersionUID = 1L;
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
