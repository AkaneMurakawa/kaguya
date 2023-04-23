package com.github.kaguya.biz.category.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 名称
     */
    private String name;
    /**
     * 展示顺序id
     */
    private Integer orderId;
}
