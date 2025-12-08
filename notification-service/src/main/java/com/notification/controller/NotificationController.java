package com.notification.controller;

import com.notification.dto.BookingDTO;
import com.notification.dto.NotificationDTO;
import com.notification.mapper.NotificationMapper;
import com.notification.model.Notification;
import com.notification.service.NotificationService;
import com.notification.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final BookingFeignClient bookingFeignClient;
    private NotificationDTO createdNotification;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody Notification notification)
            throws Exception {

        NotificationDTO createdNotification = notificationService
                .createNotification(notification);


        return ResponseEntity.ok(createdNotification);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @PathVariable Long userId) {
        List<Notification> notifications = notificationService
                .getNotificationByUserId(userId);

        List<NotificationDTO> notificationDTOS=notifications.stream().map((notification)-> {
            BookingDTO bookingDTO= null;
            try {
                bookingDTO = bookingFeignClient
                        .getBookingById(notification.getBookingId()).getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return notificationMapper.toDTO(notification,bookingDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsBysalonId(
            @PathVariable Long salonId) {
        List<Notification> notifications = notificationService
                .getNotificationByUserId(salonId);

        List<NotificationDTO> notificationDTOS=notifications.stream().map((notification)-> {
            BookingDTO bookingDTO= null;
            try {
                bookingDTO = bookingFeignClient
                        .getBookingById(notification.getBookingId()).getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return notificationMapper.toDTO(notification,bookingDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(
            @PathVariable Long notificationId) throws Exception {
        Notification updatedNotification = notificationService
                .markNotificationAsRead(notificationId);
        BookingDTO bookingDTO= bookingFeignClient
                .getBookingById(updatedNotification.getBookingId()).getBody();

        NotificationDTO notificationDTO= notificationMapper.toDTO(
                updatedNotification,
                bookingDTO
        );

        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = notificationService
                .getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
}
