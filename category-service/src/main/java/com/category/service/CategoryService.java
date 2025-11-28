package com.category.service;

import com.category.model.Category;
import com.category.payload.DTO.SalonDTO;


import java.util.Set;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);
    Set<Category> getCategoryBySalonId(Long id);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id,Long salonId) throws Exception;

}
