package com.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;

    private String type;

    private String description;

    private Boolean isRead = false;

    private Long salonId;

    private Long userId;

    private Long bookingId;

    private LocalDateTime createdAt;


}

