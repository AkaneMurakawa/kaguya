package com.github.kaguya.service.impl;

import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.DocumentGroupMapper;
import com.github.kaguya.model.DocumentGroup;
import com.github.kaguya.model.DocumentTreeDTO;
import com.github.kaguya.service.DocumentGroupService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentGroupServiceImpl implements DocumentGroupService {

    private final static String KEY_PREFIXE = "docs:tree:";
    private final static long EXPIRE = 3600 * 24L;
    private final static Integer PARENT_ID = 0;

    @Resource
    private RedisDao redisDao;
    @Resource
    private DocumentGroupMapper documentGroupMapper;

    @Override
    public DocumentTreeDTO getDocsTree(Long documentId) {
        String key = KEY_PREFIXE + documentId;
        DocumentTreeDTO documentGroups = (DocumentTreeDTO)redisDao.get(key);
        if (Objects.isNull(documentGroups)){
            // 获得分类id下所有一级文档
            List<DocumentGroup> root = documentGroupMapper.getDocsParent(documentId, PARENT_ID);
            if (CollectionUtils.isEmpty(root)){
                return null;
            }
            // 遍历树形文档
            root = getDocsGroup(root);
            documentGroups = new DocumentTreeDTO();
            documentGroups.setGroups(root);
            redisDao.set(key, documentGroups, EXPIRE);
        }
        return documentGroups;
    }

    /**
     * 遍历树形文档
     * @param root
     * @return
     */
    private List<DocumentGroup> getDocsGroup(List<DocumentGroup> root){
        for (DocumentGroup children : root) {
            List<DocumentGroup> node = documentGroupMapper.getDocsGroup(children.getDocumentId());
            if (CollectionUtils.isEmpty(node)){
                continue;
            }else {
                List<DocumentGroup> docsGroup = getDocsGroup(node);
                children.setChildren(docsGroup);
            }
        }
        return root;
    }
}
