package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Category;
import com.example.Ecommerce.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category categoryToUpdate = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + id));

        categoryToUpdate.setName(categoryDetails.getName());
        categoryToUpdate.setDescription(categoryDetails.getDescription());

        return categoryRepo.save(categoryToUpdate);
    }

    public void deleteCategory(Long id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
        } else {
            throw new RuntimeException("Category not found for ID: " + id);
        }
    }
}
