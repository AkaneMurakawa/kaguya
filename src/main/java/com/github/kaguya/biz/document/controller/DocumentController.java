package com.github.kaguya.biz.document.controller;

import com.github.kaguya.annotation.LoginPermission;
import com.github.kaguya.base.response.Result;
import com.github.kaguya.biz.document.model.entity.Document;
import com.github.kaguya.biz.document.model.dto.DocumentTreeDTO;
import com.github.kaguya.biz.document.model.dto.DocumentDTO;
import com.github.kaguya.biz.document.service.DocumentGroupService;
import com.github.kaguya.biz.document.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 文档
 */
@Controller
@RequestMapping("/docs")
public class DocumentController {

    private static final String CATEGORY_ID = "{categoryId:[0-9]{1,17}}";
    private static final String DOC_ID = "/{docId:[0-9]{1,17}}";
    private static final String CATEGORY_NAME = "{categoryName}";
    private static final String DELIMITER = "/";

    @Resource
    private DocumentGroupService documentGroupService;
    @Resource
    private DocumentService documentService;

    /**
     * 访问分类
     */
    @GetMapping(CATEGORY_NAME + DELIMITER + CATEGORY_ID)
    public ModelAndView getCategory(@PathVariable("categoryName") String categoryName, @PathVariable("categoryId") Long categoryId) {
        ModelAndView modelAndView = new ModelAndView();

        DocumentTreeDTO docsTree = documentGroupService.getDocsTree(categoryId);
        // 访问分类时的默认文档
        if (docsTree != null) {
            docsTree.setCategoryName(categoryName);
            docsTree.setCategoryId(categoryId);

            Long documentId = docsTree.getGroups().get(0).getDocumentId();
            Document doc = documentService.getDoc(documentId);
            modelAndView.addObject("doc", doc);
        }else{
            docsTree = new DocumentTreeDTO();
            docsTree.setGroups(new ArrayList<>());
            modelAndView.addObject("doc", new Document());
        }
        modelAndView.addObject("docsTree", docsTree);
        modelAndView.setViewName("docs/detail");
        return modelAndView;
    }

    /**
     * 访问文档内容，逻辑同上
     */
    @GetMapping(CATEGORY_NAME + DELIMITER + CATEGORY_ID + DELIMITER + DOC_ID)
    public ModelAndView docs(@PathVariable("categoryName") String categoryName, @PathVariable("categoryId") Long categoryId, @PathVariable("docId") Long docId) {
        ModelAndView modelAndView = new ModelAndView();

        DocumentTreeDTO docsTree = documentGroupService.getDocsTree(categoryId);
        docsTree.setCategoryName(categoryName);
        docsTree.setCategoryId(categoryId);
        Document doc = documentService.getDoc(docId);

        modelAndView.addObject("docsTree", docsTree);
        modelAndView.addObject("doc", doc);
        modelAndView.setViewName("docs/detail");
        return modelAndView;
    }

    /**
     * 跳转到新建文档页面
     */
    @LoginPermission
    @RequestMapping("addPage")
    public String addPage() {
        return "docs/addPage";
    }

    /**
     * 新建文档
     */
    @ResponseBody
    @LoginPermission
    @PostMapping("add")
    public Result add(@RequestBody DocumentDTO documentDTO) {
        documentGroupService.add(documentDTO);
        return Result.success();
    }

    /**
     * 获取分类id下的所有标题
     */
    @ResponseBody
    @GetMapping("getParentsId/" + CATEGORY_ID)
    public Result getParentsId(@PathVariable("categoryId") Long categoryId) {
        return Result.success(documentGroupService.getParents(categoryId));
    }

}
