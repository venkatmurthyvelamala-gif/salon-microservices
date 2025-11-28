package com.payment.payload.dto;

import lombok.Data;

@Data
public class ServiceDTO {

    private Long id;

    private String name;

    private String description;

    private int duration;

    private int price;

    private Long salonId;

    private Long categoryId;

    private String images;
}
