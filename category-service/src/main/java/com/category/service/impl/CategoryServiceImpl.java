package com.category.service.impl;

import com.category.model.Category;
import com.category.payload.DTO.SalonDTO;
import com.category.repository.CategoryRepository;
import com.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {

        Category category1 = new Category();
        category1.setName(category.getName());
        category1.setSalonId(salonDTO.getId());
        category1.setImages(category.getImages());
        return categoryRepository.save(category1);
    }

    @Override
    public Set<Category> getCategoryBySalonId(Long id) {
        return categoryRepository.findBySalonId(id);

    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category  category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new Exception("Category with this id not found");
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) throws Exception {

        Category category = getCategoryById(id);

        if(category.getSalonId().equals(salonId)){
            categoryRepository.deleteById(id);
        }
        throw  new Exception("you don't have permission to delete this category ");

    }
}
