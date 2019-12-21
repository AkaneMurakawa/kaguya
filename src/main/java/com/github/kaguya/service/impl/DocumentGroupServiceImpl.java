package com.github.kaguya.service.impl;

import com.github.kaguya.config.SystemProperty;
import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.DocumentGroupMapper;
import com.github.kaguya.dao.mapper.DocumentMapper;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.*;
import com.github.kaguya.service.DocumentGroupService;
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
public class DocumentGroupServiceImpl implements DocumentGroupService {

    private final static String KEY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "docs:";
    private final static long EXPIRE = 3600 * 24L;
    private final static Integer PARENT_ID = 0;

    @Resource
    private RedisDao redisDao;
    @Resource
    private DocumentGroupMapper documentGroupMapper;
    @Resource
    private DocumentMapper documentMapper;

    @Override
    public DocumentTreeDTO getDocsTree(Long categoryId) {
        String key = KEY_PREFIX + "tree:" + categoryId;
        Object object = redisDao.get(key);
        if (Objects.isNull(object)) {
            // 获得分类id下所有一级文档
            List<DocumentGroupVO> root = documentGroupMapper.getDocsParent(categoryId, PARENT_ID);
            if (CollectionUtils.isEmpty(root)) {
                return null;
            }
            // 遍历树形文档
            root = getDocsGroup(root);
            DocumentTreeDTO documentGroups = new DocumentTreeDTO();
            documentGroups.setGroups(root);
            redisDao.set(key, documentGroups, EXPIRE);
            return documentGroups;
        }
        String json = JsonUtils.objectToJson(object);
        return JsonUtils.parseObject(json, DocumentTreeDTO.class);
    }

    /**
     * 遍历树形文档
     */
    private List<DocumentGroupVO> getDocsGroup(List<DocumentGroupVO> root) {
        for (DocumentGroupVO children : root) {
            List<DocumentGroupVO> node = documentGroupMapper.getDocsGroup(children.getDocumentId());
            if (CollectionUtils.isEmpty(node)) {
                continue;
            } else {
                List<DocumentGroupVO> docsGroup = getDocsGroup(node);
                children.setChildren(docsGroup);
            }
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
        if (null == orderId) {
            return 0;
        } else {
            orderId += 1;
        }
        return orderId;
    }

    /**
     * 新增文档
     */
    @Override
    @Synchronized
    @Transactional(rollbackFor = Exception.class)
    public ResponseMsg add(DocumentVO documentVO) {
        // 设置默认的父类ID
        if (null == documentVO.getParentId()) {
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

        if (result1 < 0 || result2 < 0) {
            log.error("新增文档失败，categoryId:{}, parentId:{}", documentVO.getCategoryId(), documentVO.getParentId());
            return ResponseMsg.buildFailResult();
        }
        // 将HTML缓存到redis
        String key = KEY_PREFIX + "html:" + documentId;
        redisDao.set(key, documentVO.getContentHTML());

        // 清除doc tree的redis缓存
        redisDao.del(KEY_PREFIX + "tree:" + documentVO.getCategoryId());
        return ResponseMsg.buildSuccessResult();
    }
}
