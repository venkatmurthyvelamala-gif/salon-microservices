package com.service.service.impl;

import com.service.model.ServiceOffering;
import com.service.payload.dto.CategoryDTO;
import com.service.payload.dto.SalonDTO;
import com.service.payload.dto.ServiceDTO;
import com.service.repository.ServiceOfferingRepository;
import com.service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    @Autowired
    ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {

        ServiceOffering serviceOffering = new ServiceOffering();

        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDuration(serviceDTO.getDuration());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setImages(serviceDTO.getImages());
        serviceOffering.setCategoryId(serviceDTO.getCategory());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setSalonId(serviceDTO.getSalonId());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {

        ServiceOffering serviceOffer = serviceOfferingRepository.findById(serviceId).orElse(null);
         if(serviceOffer != null){
             serviceOffer.setName(service.getName());
             serviceOffer.setDuration(service.getDuration());
             serviceOffer.setDescription(service.getDescription());
             serviceOffer.setImages(service.getImages());
             serviceOffer.setCategoryId(service.getCategoryId());
             serviceOffer.setPrice(service.getPrice());
             serviceOffer.setSalonId(service.getSalonId());

             return serviceOfferingRepository.save(serviceOffer);
         }

         throw new Exception("service not exist with id"+serviceId);
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {

        if(categoryId == null){
            return serviceOfferingRepository.findBySalonId(salonId);
        }
        return serviceOfferingRepository.findBySalonIdAndCategoryId(salonId, categoryId);

    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
        List<ServiceOffering> serviceOffering = serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(serviceOffering);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering service = serviceOfferingRepository.findById(id).orElse(null);

        if(service == null){
            throw new Exception("service not found  with id"+id);
        }
        return service;
    }

}
