package com.github.kaguya.biz.category.controller;

import com.github.kaguya.response.Result;
import com.github.kaguya.biz.category.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 分类
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询所有分类
     */
    @GetMapping("list")
    public Result list() {
        return Result.success(categoryService.listCategories());
    }

    /**
     * 新增分类
     */
    @PostMapping("add")
    public Result add(@RequestParam(value = "name") String name) {
        categoryService.add(name);
        return Result.success();
    }

}
