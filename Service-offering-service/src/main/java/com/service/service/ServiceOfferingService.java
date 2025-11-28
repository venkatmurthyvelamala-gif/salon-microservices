package com.service.service;

import com.service.model.ServiceOffering;
import com.service.payload.dto.CategoryDTO;
import com.service.payload.dto.SalonDTO;
import com.service.payload.dto.ServiceDTO;
import jdk.jfr.Category;

import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createService(SalonDTO salonDTO,
                                  ServiceDTO serviceDTO,
                                  CategoryDTO categoryDTO);

    ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering) throws Exception;

    Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId);

    Set<ServiceOffering> getServicesByIds(Set<Long> ids);

    ServiceOffering getServiceById(Long id) throws Exception;


}
