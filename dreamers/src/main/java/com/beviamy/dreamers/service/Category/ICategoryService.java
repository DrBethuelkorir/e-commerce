package com.beviamy.dreamers.service.Category;

import com.beviamy.dreamers.models.Category;

import java.util.List;

public interface ICategoryService {
    Category findById(Long id);
    Category findByName(String name);
    List<Category> findAll();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
}
