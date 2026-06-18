package com.beviamy.dreamers.service.Category;

import com.beviamy.dreamers.Repository.CategoryRepository;
import com.beviamy.dreamers.exeption.AlreadyexistsExeptiom;
import com.beviamy.dreamers.exeption.CategoryNotFoundExeption;
import com.beviamy.dreamers.exeption.ProductNotFoundExeption;
import com.beviamy.dreamers.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundExeption("Category not found"));
    }

    @Override
    public Category findByName(String name) {
        Category category = this.categoryRepository.findByName(name);
        if (category == null) {
            throw new CategoryNotFoundExeption("Category not found with name: " + name);
        }
        return category;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository
                .existsByName(category.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() ->new AlreadyexistsExeptiom(category.getName() + "Already exist"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(findById(id))
                .map(oldCategory ->{
                    oldCategory.setName(category.getName());
                    return this.categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new CategoryNotFoundExeption("Category not found"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository ::delete,() ->{
            throw new CategoryNotFoundExeption("Category not found");
        });
    }
}
