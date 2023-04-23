package com.github.kaguya.biz.document.service;

import com.github.kaguya.biz.document.model.entity.Document;

public interface DocumentService {

    /**
     * 获取文档
     */
    Document getDoc(Long documentId);
}
