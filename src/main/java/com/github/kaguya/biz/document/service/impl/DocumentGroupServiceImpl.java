package com.github.kaguya.biz.document.service.impl;

import com.github.kaguya.biz.document.model.dto.DocumentTreeDTO;
import com.github.kaguya.biz.document.model.entity.Document;
import com.github.kaguya.biz.document.model.entity.DocumentGroup;
import com.github.kaguya.biz.document.model.dto.DocumentGroupDTO;
import com.github.kaguya.biz.document.model.dto.DocumentDTO;
import com.github.kaguya.biz.document.service.IDocumentKey;
import com.github.kaguya.base.exception.BusinessException;
import com.github.kaguya.prop.SystemProperty;
import com.github.kaguya.constant.BaseConstant;
import com.github.kaguya.biz.document.mapper.DocumentGroupMapper;
import com.github.kaguya.biz.document.mapper.DocumentMapper;
import com.github.kaguya.config.redis.client.RedisClient;
import com.github.kaguya.biz.document.service.DocumentGroupService;
import com.github.kaguya.util.JsonUtils;
import com.github.kaguya.util.SnowFlake;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DocumentGroupServiceImpl implements DocumentGroupService, IDocumentKey {

    private static final long EXPIRE = 3600 * 24L;

    @Resource
    private SystemProperty systemProperty;
    @Resource
    private RedisClient redisClient;
    @Resource
    private DocumentGroupMapper documentGroupMapper;
    @Resource
    private DocumentMapper documentMapper;

    @Override
    public DocumentTreeDTO getDocsTree(Long categoryId) {
        String key = KEY_TREE + categoryId;
        Object object = redisClient.get(key);
        if (Objects.isNull(object)) {
            // 获得分类id下所有一级文档
            List<DocumentGroupDTO> root = documentGroupMapper.getDocsParent(categoryId, BaseConstant.DOCUMENT_ROOT_ID);
            if (CollectionUtils.isEmpty(root)) {
                return null;
            }
            // 遍历树形文档
            genDocsGroup(root);
            DocumentTreeDTO documentGroups = new DocumentTreeDTO();
            documentGroups.setGroups(root);
            redisClient.set(key, documentGroups, EXPIRE);
            return documentGroups;
        }
        String json = JsonUtils.objectToJson(object);
        return JsonUtils.parseObject(json, DocumentTreeDTO.class);
    }

    /**
     * 递归生成树形文档
     */
    private List<DocumentGroupDTO> genDocsGroup(List<DocumentGroupDTO> root) {
        for (DocumentGroupDTO children : root) {
            List<DocumentGroupDTO> node = documentGroupMapper.getDocsGroup(children.getDocumentId());
            if (CollectionUtils.isEmpty(node)) {
                continue;
            }
            List<DocumentGroupDTO> docsGroup = genDocsGroup(node);
            children.setChildren(docsGroup);
        }
        return root;
    }

    /**
     * 获得父类id
     */
    @Override
    public List<DocumentGroup> getParents(Long categoryId) {
        return documentGroupMapper.getParents(categoryId);
    }

    /**
     * 获取分类下当前父类下的下一个orderId
     */
    public Integer getNextOrderId(Long categoryId, Long parentId) {
        // 获取最大的orderId
        Integer orderId = documentGroupMapper.getMaxOrderIdBy(categoryId, parentId);
        return null == orderId ? 0 : orderId + 1;
    }

    /**
     * 新增文档
     */
    @Override
    @Synchronized
    @Transactional(rollbackFor = Exception.class)
    public void add(DocumentDTO documentDTO) {
        Integer orderId = getNextOrderId(documentDTO.getCategoryId(), documentDTO.getParentId());
        Long documentId = SnowFlake.generateId();

        DocumentGroup documentGroup = new DocumentGroup()
                .setDocumentId(documentId)
                .setCategoryId(documentDTO.getCategoryId())
                .setParentId(documentDTO.getParentId())
                .setTitle(documentDTO.getTitle())
                .setOrderId(orderId);
        int result1 = documentGroupMapper.insert(documentGroup);

        Document document = new Document()
                .setDocumentId(documentId)
                .setContent(documentDTO.getContent())
                .setCreateUser(systemProperty.getAuthor());
        int result2 = documentMapper.insert(document);

        if (result1 < 0 || result2 < 0) {
            log.error("新增文档失败，categoryId:{}, parentId:{}", documentDTO.getCategoryId(), documentDTO.getParentId());
            throw new BusinessException("新增文档失败");
        }
        // 将HTML缓存到redis
        String key = KEY_HTML + documentId;
        redisClient.set(key, documentDTO.getContentHTML());

        // 清除doc tree的redis缓存
        redisClient.del(KEY_TREE + documentDTO.getCategoryId());
    }
}
