package com.category.controller;

import com.category.model.Category;
import com.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getCategoryBySalon(@PathVariable Long id){

        Set<Category> categories = categoryService.getCategoryBySalonId(id);
        return new ResponseEntity<>(categories, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) throws Exception {

        Category category = categoryService.getCategoryById(id);

        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }

    @GetMapping("/salon/{id}/category/{id}")
    public ResponseEntity<Category> getCategoryByIdAndSalonId(@PathVariable Long id,
                                                              @PathVariable Long salonId) throws Exception {

        Category category = categoryService.getCategoryByIdAndSalonId(id, salonId);
        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }

}
