package com.service.controller;

import com.service.model.ServiceOffering;
import com.service.payload.dto.CategoryDTO;
import com.service.payload.dto.SalonDTO;
import com.service.payload.dto.ServiceDTO;
import com.service.service.ServiceOfferingService;
import com.service.service.client.CategoryFeignClient;
import com.service.service.client.SalonFeignClient;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

    @Autowired
    ServiceOfferingService serviceOfferingService;

    @Autowired
    SalonFeignClient salonFeignClient;

    @Autowired
    CategoryFeignClient categoryFeignClient;

    @PostMapping("/create")
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO,
            @RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        if(salonDTO == null){
            throw new Exception("salon not found");
        }

        CategoryDTO categoryDTO = categoryFeignClient.getCategoryByIdAndSalonId(serviceDTO.getCategory(), salonDTO.getId()).getBody();


        ServiceOffering serviceOfferings = serviceOfferingService.createService(salonDTO,serviceDTO,categoryDTO);
        return ResponseEntity.ok(serviceOfferings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Long id,
            @RequestBody ServiceOffering serviceOffering) throws Exception {

        ServiceOffering updateServiceOffering = serviceOfferingService.updateService(id,serviceOffering);
        return ResponseEntity.ok(updateServiceOffering);

    }






}
