package com.github.kaguya.service.impl;

import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.DocumentMapper;
import com.github.kaguya.model.Document;
import com.github.kaguya.service.DocumentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final static String KEY_PREFIXE = "docs:content:";
    private final static long EXPIRE = 3600 * 24L;

    @Resource
    private RedisDao redisDao;
    @Resource
    private DocumentMapper documentMapper;

    /**
     * 获得文档的内容
     * @param documentId
     * @return
     */
    @Override
    public Document getDoc(Long documentId) {
        String key = KEY_PREFIXE + documentId;
        Document document = (Document) redisDao.get(key);
        if (Objects.isNull(document)){
            Document example = new Document();
            example.setDocumentId(documentId);
            document = documentMapper.selectOne(example);
            redisDao.set(key, document, EXPIRE);
        }
        return document;
    }
}
