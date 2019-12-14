package com.github.kaguya.controller;

import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Category;
import com.github.kaguya.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("add")
    @ResponseBody
    public ResponseMsg add(@RequestBody Category category){
        return categoryService.add(category);
    }

    @ResponseBody
    @GetMapping("list")
    public ResponseMsg list() {
        return ResponseMsg.buildSuccessResult(categoryService.listCategories());
    }
}
