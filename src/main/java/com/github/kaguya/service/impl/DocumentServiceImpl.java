package com.github.kaguya.service.impl;

import com.github.kaguya.constant.CommonConstant;
import com.github.kaguya.dao.RedisDao;
import com.github.kaguya.dao.mapper.DocumentMapper;
import com.github.kaguya.model.Document;
import com.github.kaguya.service.DocumentService;
import com.github.kaguya.util.JsonUtils;
import com.github.kaguya.util.MarkdownUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final static String KEY_PREFIX = CommonConstant.REDIS_KEY_PREFIX_KAGUYA_WEB + "docs:";
    private final static long EXPIRE = 3600 * 24L;

    @Resource
    private RedisDao redisDao;
    @Resource
    private DocumentMapper documentMapper;

    /**
     * 获得文档的内容
     */
    @Override
    public Document getDoc(Long documentId) {
        String tempContent = "";
        String key = KEY_PREFIX + "content:" + documentId;
        Object object = redisDao.get(key);

        String contentHTML = getContentHTML(documentId);
        if (Objects.isNull(object) || StringUtils.isEmpty(contentHTML)) {
            Document document = new Document();
            document.setDocumentId(documentId);
            document = documentMapper.selectOne(document);
            // 临时存储Markdown内容
            tempContent = document.getContent();
            // content取html
            document.setContent("");
            redisDao.set(key, document, EXPIRE);

            // 设置content
            if (StringUtils.isEmpty(contentHTML)) {
                contentHTML = MarkdownUtil.MarkdownToHtml(tempContent);
                // 将HTML缓存到redis
                redisDao.set(KEY_PREFIX + "html:" + documentId, contentHTML);
            }
            document.setContent(contentHTML);
            return document;
        }
        String json = JsonUtils.objectToJson(object);
        Document document = JsonUtils.jsonToObject(json, Document.class);


        return document;
    }

    public String getContentHTML(Long documentId) {
        return (String) redisDao.get(KEY_PREFIX + "html:" + documentId);
    }
}
