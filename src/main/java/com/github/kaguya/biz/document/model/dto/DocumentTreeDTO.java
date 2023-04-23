package com.github.kaguya.biz.document.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DocumentTreeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;

    List<DocumentGroupDTO> groups;
}
