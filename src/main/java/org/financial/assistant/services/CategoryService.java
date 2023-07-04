package org.financial.assistant.services;

import org.financial.assistant.models.Category;
import org.financial.assistant.models.User;
import org.financial.assistant.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategoriesForUser(User user) {
        return categoryRepository.findByUserId(user.getId());
    }

    @Transactional
    public void createCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this category");
        }

        categoryRepository.delete(category);
    }

}
