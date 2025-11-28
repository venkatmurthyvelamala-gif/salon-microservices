package com.category.payload.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalonDTO {

    private Long id;

    private String name;

    private List<String> images;

    private String location;

    private String city;

    private Long ownerId;

    private String phoneNumber;

    private LocalDateTime openingTime;

    private LocalDateTime closingTime;

}
