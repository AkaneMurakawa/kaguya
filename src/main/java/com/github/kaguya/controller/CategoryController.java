package com.github.kaguya.controller;

import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
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
    @ResponseBody
    @PostMapping("add")
    public ResponseMsg add(@RequestParam(value = "name") String name) {
        if (StringUtils.isEmpty(name)){
            return ResponseMsg.buildFailResult("该项不能为空");
        }
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
