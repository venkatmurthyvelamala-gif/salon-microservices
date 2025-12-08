package com.salon.controller;

import com.salon.mapper.SalonMapper;
import com.salon.model.Salon;
import com.salon.payload.DTO.SalonDTO;
import com.salon.payload.DTO.UserDTO;
import com.salon.service.SalonService;
import com.salon.service.client.UserFeignClient;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
public class SalonController {

    @Autowired
    SalonService salonService;

    @Autowired
    UserFeignClient userFeignClient;

    @PostMapping("/create")
    public ResponseEntity<SalonDTO> createSalon(
            @RequestBody SalonDTO salonDTO,
            @RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO userDTO = userFeignClient.getUserFromJwtToken(jwt).getBody();

        Salon createSalons = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salon = SalonMapper.mapToDto(createSalons);
        return new ResponseEntity<>(salon, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestBody SalonDTO salonDTO,
                                                @PathVariable Long id,
                                                @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserFromJwtToken(jwt).getBody();

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
    public ResponseEntity<SalonDTO> getSalonByOwnerId(@RequestHeader("Authorization")
                                                          String jwt) throws Exception {

        UserDTO userDto = userFeignClient.getUserFromJwtToken(jwt).getBody();
        if (userDto == null) {
            throw  new Exception("user not found"+ userDto);
        }
        Salon salon = salonService.getSalonByOwnerId(userDto.getId());

        SalonDTO salonDTO = SalonMapper.mapToDto(salon);

        return new ResponseEntity<>(salonDTO, HttpStatus.FOUND);
    }
}
