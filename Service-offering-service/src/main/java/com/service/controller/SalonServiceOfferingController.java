package com.service.controller;

import com.service.model.ServiceOffering;
import com.service.payload.dto.CategoryDTO;
import com.service.payload.dto.SalonDTO;
import com.service.payload.dto.ServiceDTO;
import com.service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

    @Autowired
    ServiceOfferingService serviceOfferingService;

    @PostMapping("/create")
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO) {

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.getId();

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(Math.toIntExact(serviceDTO.getCategoryId()));

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
