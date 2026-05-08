package com.sharedbalance.sharedbalancebackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiverEmail;
    private String senderEmail; // who triggered it

    private String type; 
    // PAYMENT_RECEIVED, PAYMENT_PAID, GROUP_CREATED, EXPENSE_ADDED

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;
}
