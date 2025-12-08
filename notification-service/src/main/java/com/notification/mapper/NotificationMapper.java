package com.notification.mapper;

import com.notification.dto.BookingDTO;
import com.notification.dto.NotificationDTO;
import com.notification.model.Notification;
import lombok.*;


public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification,
                                 BookingDTO bookingDTO){

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setDescription(notification.getDescription());
        notificationDTO.setIsRead(notification.getIsRead());
        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setBookingId(bookingDTO.getId());
        notificationDTO.setSalonId(notification.getSalonId());


        return notificationDTO;
    }
}
