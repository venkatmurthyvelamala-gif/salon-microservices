package com.salon.service;

import com.salon.model.Salon;
import com.salon.payload.DTO.SalonDTO;
import com.salon.payload.DTO.UserDTO;

import java.util.List;

public interface SalonService {

    Salon createSalon(SalonDTO salon, UserDTO user);

    Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception;

    List<Salon> getAllSalon();

    Salon getSalonById(Long id) throws Exception;

    Salon getSalonByOwnerId(Long ownerId);

    List<Salon> searchSalonByCity(String city);
}
