package com.salon.controller;

import com.salon.mapper.SalonMapper;
import com.salon.model.Salon;
import com.salon.payload.DTO.SalonDTO;
import com.salon.payload.DTO.UserDTO;
import com.salon.service.SalonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salon")
public class SalonController {

    @Autowired
    SalonService salonService;

    @PostMapping("/create")
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        Salon createSalons = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salon = SalonMapper.mapToDto(createSalons);
        return new ResponseEntity<>(salon, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestBody SalonDTO salonDTO,
                                                @PathVariable Long id) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        Salon updateSalons = salonService.updateSalon(salonDTO, userDTO, id);
        SalonDTO salon = SalonMapper.mapToDto(updateSalons);
        return new ResponseEntity<>(salon, HttpStatus.OK);
    }

    @GetMapping("/allSalons")
    public ResponseEntity<List<SalonDTO>> getAllSalons(){

        List<Salon> salons = salonService.getAllSalon();

        List<SalonDTO> salonDTO = salons.stream().map(salon ->
        {
            SalonDTO salondto = SalonMapper.mapToDto(salon);
            return salondto;
        }).toList();

        return new ResponseEntity<>(salonDTO, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable("id") Long salonId) throws Exception {

        Salon salon = salonService.getSalonById(salonId);
        SalonDTO salonDTO = SalonMapper.mapToDto(salon);
        return new ResponseEntity<>(salonDTO, HttpStatus.FOUND);

    }


    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(@RequestParam String city){

        List<Salon> salon = salonService.searchSalonByCity(city);

        List<SalonDTO> salonDTO = salon.stream().map(salon1 -> {
            SalonDTO salonDTOs = SalonMapper.mapToDto(salon1);
            return salonDTOs;
        }).toList();
        return new ResponseEntity<>(salonDTO, HttpStatus.FOUND);

    }

    @GetMapping("/owner/{Id}")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(@PathVariable Long ownerId){

        UserDTO userDto = new UserDTO();
        userDto.setId(1L);
        Salon salon = salonService.getSalonByOwnerId(userDto.getId());

        SalonDTO salonDTO = SalonMapper.mapToDto(salon);

        return new ResponseEntity<>(salonDTO, HttpStatus.FOUND);
    }
}
