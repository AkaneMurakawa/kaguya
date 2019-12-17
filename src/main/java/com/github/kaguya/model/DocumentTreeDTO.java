package com.github.kaguya.model;

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

    List<DocumentGroup> groups;
}
