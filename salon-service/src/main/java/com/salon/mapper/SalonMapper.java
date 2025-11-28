package com.salon.mapper;

import com.salon.model.Salon;
import com.salon.payload.DTO.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToDto(Salon salon){

        SalonDTO salonDTO = new SalonDTO();

        salonDTO.setId(salon.getId());
        salonDTO.setCity(salon.getCity());
        salonDTO.setName(salon.getName());
        salonDTO.setImages(salon.getImages());
        salonDTO.setLocation(salon.getLocation());
        salonDTO.setPhoneNumber(salon.getPhoneNumber());
        salonDTO.setOpeningTime(salon.getOpeningTime());
        salonDTO.setClosingTime(salon.getClosingTime());
        salonDTO.setOwnerId(salon.getOwnerId());

        return salonDTO;
    }
}
