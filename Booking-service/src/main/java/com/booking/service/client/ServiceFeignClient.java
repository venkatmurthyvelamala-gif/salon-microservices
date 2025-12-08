package com.booking.service.client;

import com.booking.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("SERVICE-OFFERING-SERVICE")
public interface ServiceFeignClient {

    @GetMapping("api/service-offering/list/{ids}")
    public ResponseEntity<Set<ServiceDTO>> getServiceByIds(@PathVariable Set<Long> ids);

    }
