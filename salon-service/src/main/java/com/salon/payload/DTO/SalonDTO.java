package com.salon.payload.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
