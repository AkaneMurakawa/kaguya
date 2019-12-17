package com.github.kaguya.service;

import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.DocumentGroup;
import com.github.kaguya.model.DocumentTreeDTO;
import com.github.kaguya.model.DocumentVO;

import java.util.List;

public interface DocumentGroupService {

    DocumentTreeDTO getDocsTree(Long categoryId);

    List<DocumentGroup> getParents(Long categoryId);

    ResponseMsg add(DocumentVO documentVO);
}
