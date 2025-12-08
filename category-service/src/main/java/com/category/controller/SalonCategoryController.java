package com.category.controller;

import com.category.model.Category;
import com.category.payload.DTO.SalonDTO;
import com.category.service.CategoryService;
import com.category.service.client.SalonFeignClient;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category/salon-owner")
public class SalonCategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    SalonFeignClient salonFeignClient;

    @PostMapping("/create")
    public ResponseEntity<Category> saveCategory(@RequestBody Category category,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
       SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        Category category1 = categoryService.saveCategory(category, salonDTO);

        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        if(salonDTO == null){
            throw new Exception("user not found "+ salonDTO);
        }

        categoryService.deleteCategoryById(id, salonDTO.getId());

        return ResponseEntity.ok("Category deleted Successfully");
    }
}
