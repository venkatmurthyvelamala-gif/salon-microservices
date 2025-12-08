package com.notification.service.impl;

import com.notification.dto.BookingDTO;
import com.notification.dto.NotificationDTO;
import com.notification.mapper.NotificationMapper;
import com.notification.model.Notification;
import com.notification.repository.NotificationRepository;
import com.notification.service.NotificationService;
import com.notification.service.client.BookingFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    BookingFeignClient bookingFeignClient;

    @Override
    public NotificationDTO createNotification(Notification notification) throws Exception {

        Notification savedNotification = notificationRepository.save(notification);

        BookingDTO bookingDTO = bookingFeignClient.getBookingById(savedNotification.getBookingId()).getBody();

        NotificationDTO notificationDTO = NotificationMapper.toDTO(savedNotification,bookingDTO);

        return notificationDTO;
    }

    @Override
    public List<Notification> getNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationBySalonId(Long salonId) {
        return notificationRepository.findBySalonId(salonId);
    }

    @Override
    public Notification markNotificationAsRead(Long notificationId) throws Exception {
        return notificationRepository.findById(notificationId).map(
                notification -> {
                    notification.setIsRead(true);

                    return notificationRepository.save(notification);
                }
        ).orElseThrow(()-> new Exception("Notification not found"));
    }

    @Override
    public List<Notification> getAllNotifications() {

        List<Notification> notificationList = notificationRepository.findAll();

        return notificationList;
    }
}
