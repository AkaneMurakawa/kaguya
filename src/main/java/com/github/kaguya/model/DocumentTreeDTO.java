package com.github.kaguya.model;

import lombok.Data;

import java.util.List;

@Data
public class DocumentTreeDTO {
    /**
     * 分类id
     */
    private Long categoryId;

    List<DocumentGroup> groups;
}
