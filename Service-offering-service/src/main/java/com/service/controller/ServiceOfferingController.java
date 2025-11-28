package com.service.controller;

import com.service.model.ServiceOffering;
import com.service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/service-offering")
public class ServiceOfferingController {

    @Autowired
    ServiceOfferingService serviceOfferingService;


    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(@PathVariable Long salonId,
                                                                    @RequestParam(required = false) Long categoryId){

        Set<ServiceOffering> services = serviceOfferingService.getAllServiceBySalonId(salonId, categoryId);
        return new ResponseEntity<>(services, HttpStatus.OK);

    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServiceByIds(@PathVariable Set<Long> ids){

        Set<ServiceOffering> services = serviceOfferingService.getServicesByIds(ids);
        return new ResponseEntity<>(services,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffering> getAServiceById(@PathVariable Long id) throws Exception {

        ServiceOffering serviceOffering =serviceOfferingService.getServiceById(id);
        return new ResponseEntity<>(serviceOffering, HttpStatus.OK);
    }
}
