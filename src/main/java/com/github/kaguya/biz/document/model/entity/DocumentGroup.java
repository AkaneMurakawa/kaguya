package com.github.kaguya.biz.document.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class DocumentGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 文档id
     */
    private Long documentId;
    /**
     * 父类id
     */
    private Long parentId;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 展示顺序id
     */
    private Integer orderId;
    /**
     * 标题
     */
    private String title;
    /**
     * 子类
     */
    @Transient
    private List<DocumentGroup> children = null;
}
