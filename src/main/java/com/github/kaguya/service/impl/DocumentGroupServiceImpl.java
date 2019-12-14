package com.github.kaguya.service.impl;

import com.github.kaguya.config.SystemProperty;
import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.DocumentGroupMapper;
import com.github.kaguya.dao.mapper.DocumentMapper;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Document;
import com.github.kaguya.model.DocumentGroup;
import com.github.kaguya.model.DocumentTreeDTO;
import com.github.kaguya.model.DocumentVO;
import com.github.kaguya.service.DocumentGroupService;
import com.github.kaguya.util.SnowFlake;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentGroupServiceImpl implements DocumentGroupService {

    private final static String KEY_PREFIXE = "docs:";
    private final static long EXPIRE = 3600 * 24L;
    private final static Integer PARENT_ID = 0;

    @Resource
    private RedisDao redisDao;
    @Resource
    private DocumentGroupMapper documentGroupMapper;
    @Resource
    private DocumentMapper documentMapper;

    @Override
    public DocumentTreeDTO getDocsTree(Long documentId) {
        String key = KEY_PREFIXE + "tree:" + documentId;
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

    /**
     * 获得父类id
     * @param categoryId
     * @return
     */
    @Override
    public List<DocumentGroup> getParents(Long categoryId) {
        return documentGroupMapper.getParents(categoryId);
    }

    /**
     * 获取下一个orderId
     * @param categoryId
     * @param parentId
     * @return
     */
    public Integer getNextOrderId(Long categoryId, Long parentId){
        // 获取最大的orderId
        Integer orderId = documentGroupMapper.getMaxOrderIdBy(categoryId, parentId);
        if (null == orderId){
            return 0;
        }else {
            orderId += 1;
        }
        return orderId;
    }

    /**
     * 新增文档
     * @param documentVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public ResponseMsg add(DocumentVO documentVO) {
        // 设置默认的父类ID
        if (null == documentVO.getParentId()){
            documentVO.setParentId(CommonConstant.DEFAULT_PARENT_ID);
        }

        Integer orderId = getNextOrderId(documentVO.getCategoryId(), documentVO.getParentId());
        Long documentId = SnowFlake.generateId();

        DocumentGroup documentGroup = new DocumentGroup()
                .setDocumentId(documentId)
                .setCategoryId(documentVO.getCategoryId())
                .setParentId(documentVO.getParentId())
                .setTitle(documentVO.getTitle())
                .setOrderId(orderId);
        int result1 = documentGroupMapper.insert(documentGroup);

        Document document = new Document()
                .setDocumentId(documentId)
                .setContent(documentVO.getContent())
                .setCreateUser(SystemProperty.getAuthor());
        int result2 = documentMapper.insert(document);

        if (result1 < 0 || result2 < 0){
            return ResponseMsg.buildFailResult();
        }
        // 将HTML缓存到redis
        String key = KEY_PREFIXE + "html:" + documentId;
        redisDao.set(key, documentVO.getContentHTML());
        return ResponseMsg.buildSuccessResult();
    }
}
