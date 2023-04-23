package com.github.kaguya.biz.document.mapper;

import com.github.kaguya.mapper.BaseMapper;
import com.github.kaguya.biz.document.model.entity.DocumentGroup;
import com.github.kaguya.biz.document.model.dto.DocumentGroupDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentGroupMapper extends BaseMapper<DocumentGroup> {

    /**
     * 获得分类id下所有一级文档，按order_id升序
     */
    List<DocumentGroupDTO> getDocsParent(@Param("categoryId") Long categoryId, @Param("root") Long root);

    /**
     * 获得文档id下所有文档，按order_id升序
     */
    List<DocumentGroupDTO> getDocsGroup(@Param("documentId") Long documentId);

    /**
     * 获取该分类id下的所有标题
     */
    List<DocumentGroup> getParents(@Param("categoryId") Long categoryId);

    /**
     * 获得分类id下最大的orderId
     */
    Integer getMaxOrderIdBy(@Param("categoryId") Long categoryId, @Param("parentId") Long parentId);
}
