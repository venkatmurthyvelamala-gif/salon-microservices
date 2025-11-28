package com.booking.dto;

import com.booking.domain.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO{
        
        private Long id;

        private Long salonId;

        private Long customerId;

        private LocalDateTime startTime;

        private LocalDateTime endTime;
        
        private Set<Long> serviceIds;

        private BookingStatus status = BookingStatus.PENDING;

        private Double totalPrice;
    }

