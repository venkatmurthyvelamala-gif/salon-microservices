package com.notification.service;

import com.notification.dto.NotificationDTO;
import com.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    NotificationDTO createNotification(Notification notification) throws Exception;
    List<Notification> getNotificationByUserId(Long userId);
    List<Notification> getNotificationBySalonId(Long salonId);
    Notification markNotificationAsRead(Long notificationId) throws Exception;
    List<Notification> getAllNotifications();
}
