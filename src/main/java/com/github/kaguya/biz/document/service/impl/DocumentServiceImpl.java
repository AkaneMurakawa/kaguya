package com.github.kaguya.biz.document.service.impl;

import com.github.kaguya.biz.document.mapper.DocumentMapper;
import com.github.kaguya.biz.document.model.entity.Document;
import com.github.kaguya.biz.document.service.DocumentService;
import com.github.kaguya.biz.document.service.IDocumentKey;
import com.github.kaguya.config.redis.client.RedisClient;
import com.github.kaguya.util.JsonUtils;
import com.github.kaguya.util.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService, IDocumentKey {


    private static final long EXPIRE = 3600 * 24L;

    @Resource
    private RedisClient redisClient;
    @Resource
    private DocumentMapper documentMapper;

    /**
     * 获得文档的内容
     */
    @Override
    public Document getDoc(Long documentId) {
        String tempContent;
        String key = KEY_CONTENT + documentId;
        Object object = redisClient.get(key);

        String contentHTML = getContentHTML(documentId);
        if (Objects.isNull(object) || StringUtils.isEmpty(contentHTML)) {
            Document document = new Document();
            document.setDocumentId(documentId);
            document = documentMapper.selectOne(document);
            // 临时存储Markdown内容
            tempContent = document.getContent();
            // content取html
            document.setContent("");
            redisClient.set(key, document, EXPIRE);

            // 设置content
            if (StringUtils.isEmpty(contentHTML)) {
                contentHTML = MarkdownUtil.markdownToHtml(tempContent);
                // 将HTML缓存到redis
                redisClient.set(KEY_PREFIX + "html:" + documentId, contentHTML);
            }
            document.setContent(contentHTML);
            return document;
        }
        String json = JsonUtils.objectToJson(object);
        Document document = JsonUtils.jsonToObject(json, Document.class);
        document.setContent(contentHTML);
        return document;
    }

    private String getContentHTML(Long documentId) {
        return (String) redisClient.get(KEY_HTML + documentId);
    }
}
