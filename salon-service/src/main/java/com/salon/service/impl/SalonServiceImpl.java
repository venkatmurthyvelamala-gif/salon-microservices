package com.salon.service.impl;

import com.salon.model.Salon;
import com.salon.payload.DTO.SalonDTO;
import com.salon.payload.DTO.UserDTO;
import com.salon.repository.SalonRepository;
import com.salon.service.SalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonServiceImpl implements SalonService {

    @Autowired
    SalonRepository salonRepository;
    @Override
    public Salon createSalon(SalonDTO salondto, UserDTO user) {

        Salon salon = new Salon();
        salon.setName(salondto.getName());
        salon.setCity(salondto.getCity());
        salon.setImages(salondto.getImages());
        salon.setLocation(salondto.getLocation());
        salon.setOwnerId(user.getId());
        salon.setOpeningTime(salondto.getOpeningTime());
        salon.setClosingTime(salondto.getClosingTime());
        salon.setPhoneNumber(salondto.getPhoneNumber());

        return salonRepository.save(salon);

    }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId)
                .orElseThrow(() -> new Exception("Salon not found"));
        if(existingSalon.getOwnerId().equals(user.getId())){
            existingSalon.setName(salon.getName());
            existingSalon.setPhoneNumber(salon.getPhoneNumber());
            existingSalon.setImages(salon.getImages());
            existingSalon.setOwnerId(user.getId());
            existingSalon.setLocation(salon.getLocation());
            existingSalon.setCity(salon.getCity());
            existingSalon.setOpeningTime(salon.getOpeningTime());
            existingSalon.setClosingTime(salon.getClosingTime());

            return salonRepository.save(existingSalon);
        }
        throw new Exception("You are not authorized to update this salon");
    }

    @Override
    public List<Salon> getAllSalon() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long id) throws Exception {
        Salon salonId = salonRepository.findById(id).orElse(null);
        if(salonId == null){
            throw new Exception("Salon with this id not found");
        }
        return salonId;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.searchSalons(city);
    }
}
