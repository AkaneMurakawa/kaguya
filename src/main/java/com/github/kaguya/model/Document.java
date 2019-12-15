package com.github.kaguya.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long tid;
    /**
     * 文档id
     */
    private Long documentId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 内容
     */
    private String content;
    /**
     * 浏览次数
     */
    private long views;
}
