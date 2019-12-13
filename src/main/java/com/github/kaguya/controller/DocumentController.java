package com.github.kaguya.controller;

import com.github.kaguya.exception.BusinessException;
import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Document;
import com.github.kaguya.model.DocumentDTO;
import com.github.kaguya.model.DocumentTreeDTO;
import com.github.kaguya.service.DocumentGroupService;
import com.github.kaguya.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
@RequestMapping("/docs")
public class DocumentController {

    protected static final String CATEGORY_ID = "{categoryId:[0-9]{1,17}}";
    protected static final String DOC_ID = "/{docId:[0-9]{1,17}}";
    protected static final String DOC_NAME = "/{docName}";

    @Resource
    private DocumentGroupService documentGroupService;
    @Resource
    private DocumentService documentService;

    /**
     * 访问分类
     * @param categoryId
     * @return
     */
    @GetMapping(DOC_NAME + "/" + CATEGORY_ID)
    public ModelAndView getCategory(@PathVariable("categoryId") Long categoryId){
        ModelAndView modelAndView = new ModelAndView();

        DocumentTreeDTO docsTree = documentGroupService.getDocsTree(categoryId);
        if (!Objects.isNull(docsTree)){
            Long documentId = docsTree.getGroups().get(0).getDocumentId();
            Document doc = documentService.getDoc(documentId);
            modelAndView.addObject("doc", doc);
        }else{
            throw new BusinessException(ResponseMsg.buildFailResult("category not found"));
        }
        modelAndView.addObject("docsTree", docsTree);
        modelAndView.setViewName("docs/detail");
        return modelAndView;
    }

    /**
     * 访问文档内容
     * @param categoryId
     * @return
     */
    @GetMapping(DOC_NAME + "/" + CATEGORY_ID + "/" + DOC_ID)
    public ModelAndView docs(@PathVariable("categoryId") Long categoryId, @PathVariable("docId") Long docId){
        ModelAndView modelAndView = new ModelAndView();

        DocumentTreeDTO docsTree = documentGroupService.getDocsTree(categoryId);
        Document doc = documentService.getDoc(docId);

        modelAndView.addObject("docsTree", docsTree);
        modelAndView.addObject("doc", doc);
        modelAndView.setViewName("docs/detail");
        return modelAndView;
    }

    @RequestMapping("addPage")
    public String addPage(){
        return "docs/addPage";
    }

    @PostMapping("add")
    public String add(@RequestBody DocumentDTO documentDTO){

        return "index";
    }

}
