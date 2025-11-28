package com.category.controller;

import com.category.model.Category;
import com.category.payload.DTO.SalonDTO;
import com.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category/salon-owner")
public class SalonCategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Category> saveCategory(@RequestBody Category category){
       SalonDTO salonDTO = new SalonDTO();

        salonDTO.setId(1L);
        Category category1 = categoryService.saveCategory(category, salonDTO);

        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws Exception {

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        categoryService.deleteCategoryById(id, salonDTO.getId());

        return ResponseEntity.ok("Category deleted Successfully");
    }
}
