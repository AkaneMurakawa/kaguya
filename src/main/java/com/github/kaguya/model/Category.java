package com.github.kaguya.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long tid;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 展示顺序id
     */
    private Integer orderId;
    /**
     * 名称
     */
    private String name;

}
