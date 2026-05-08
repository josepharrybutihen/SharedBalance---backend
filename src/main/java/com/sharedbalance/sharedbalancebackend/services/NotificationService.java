package com.sharedbalance.sharedbalancebackend.services;

import com.sharedbalance.sharedbalancebackend.entity.Notification;
import com.sharedbalance.sharedbalancebackend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void send(String receiver, String sender, String type, String message) {

        Notification notif = Notification.builder()
                .receiverEmail(receiver)
                .senderEmail(sender)
                .type(type)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notif);

        // 🔥 real-time push
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiver,
                message
        );
    }
}
