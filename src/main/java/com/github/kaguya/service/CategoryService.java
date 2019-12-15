package com.github.kaguya.service;

import com.github.kaguya.exception.model.ResponseMsg;
import com.github.kaguya.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listCategories();

    ResponseMsg add(String name);

}
