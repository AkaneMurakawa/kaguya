package com.github.kaguya.biz.document.model.dto;

import com.github.kaguya.constant.BaseConstant;
import lombok.Data;

import java.io.Serializable;

@Data
public class DocumentDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 父类id
     */
    private Long parentId = BaseConstant.DOCUMENT_ROOT_ID;
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
