package com.github.kaguya.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DocumentVO implements Serializable {
    /**
     * 父类id
     */
    private Long parentId;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容Markdown
     */
    private String content;
    /**
     * 内容Html
     */
    private String contentHTML;
}
