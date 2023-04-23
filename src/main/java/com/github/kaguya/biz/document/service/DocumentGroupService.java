package com.github.kaguya.biz.document.service;

import com.github.kaguya.biz.document.model.entity.DocumentGroup;
import com.github.kaguya.biz.document.model.dto.DocumentTreeDTO;
import com.github.kaguya.biz.document.model.dto.DocumentDTO;
import java.util.List;

public interface DocumentGroupService {

    /**
     * 获取文档树
     */
    DocumentTreeDTO getDocsTree(Long categoryId);

    /**
     * 获取分类id下的所有标题
     */
    List<DocumentGroup> getParents(Long categoryId);

    /**
     * 新建文档
     */
    void add(DocumentDTO documentDTO);
}
