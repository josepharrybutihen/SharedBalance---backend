package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.entity.Notification;
import com.sharedbalance.sharedbalancebackend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://shared-balance-azure.vercel.app"
})
public class NotificationController {

    private final NotificationRepository notificationRepository;

    // ✅ Get all notifications of user
    @GetMapping
    public List<Notification> getUserNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return notificationRepository.findByReceiverEmailOrderByCreatedAtDesc(email);
    }

    // ✅ Mark as read
    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notif.setRead(true);
        notificationRepository.save(notif);

        return "Marked as read";
    }

    @PutMapping("/mark-read")
    public ResponseEntity<?> markAllAsRead() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Notification> notifications =
            notificationRepository.findByReceiverEmailOrderByCreatedAtDesc(email);

        for (Notification n : notifications) {
            n.setRead(true);
        }

        notificationRepository.saveAll(notifications);

        return ResponseEntity.ok("All notifications marked as read");
    }
}
