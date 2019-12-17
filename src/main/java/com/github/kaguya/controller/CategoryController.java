package com.github.kaguya.controller;

import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping("add")
    @ResponseBody
    public ResponseMsg add(@RequestParam String name) {
        return categoryService.add(name);
    }

    /**
     * 查询所有分类
     */
    @ResponseBody
    @GetMapping("list")
    public ResponseMsg list() {
        return ResponseMsg.buildSuccessResult(categoryService.listCategories());
    }
}
