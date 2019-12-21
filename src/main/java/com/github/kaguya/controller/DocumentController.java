package com.github.kaguya.controller;

import com.github.kaguya.annotation.LoginPermission;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Document;
import com.github.kaguya.model.DocumentTreeDTO;
import com.github.kaguya.model.DocumentVO;
import com.github.kaguya.service.DocumentGroupService;
import com.github.kaguya.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;

@Controller
@RequestMapping("/docs")
public class DocumentController {

    protected static final String CATEGORY_ID = "{categoryId:[0-9]{1,17}}";
    protected static final String DOC_ID = "/{docId:[0-9]{1,17}}";
    protected static final String CATEGORY_NAME = "{categoryName}";
    protected static final String DELIMITE = "/";

    @Resource
    private DocumentGroupService documentGroupService;
    @Resource
    private DocumentService documentService;

    /**
     * 访问分类
     */
    @GetMapping(CATEGORY_NAME + DELIMITE + CATEGORY_ID)
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
    @GetMapping(CATEGORY_NAME + DELIMITE + CATEGORY_ID + DELIMITE + DOC_ID)
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
    public ResponseMsg add(@RequestBody DocumentVO documentVO) {
        return documentGroupService.add(documentVO);
    }

    /**
     * 获取分类id下的所有标题
     */
    @ResponseBody
    @GetMapping("getParentsId/" + CATEGORY_ID)
    public ResponseMsg getParentsId(@PathVariable("categoryId") Long categoryId) {
        return ResponseMsg.buildSuccessResult(documentGroupService.getParents(categoryId));
    }

}
