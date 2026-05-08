package com.sharedbalance.sharedbalancebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId; // ✅ IMPORTANT
    private String groupName;

    private String name; // who did it
    private String transactionType;

    private LocalDate date;
}
