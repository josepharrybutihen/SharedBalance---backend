package com.sharedbalance.sharedbalancebackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    private String payer;     // person who owes
    private String receiver;  // person who gets paid

    private double amount;

    private boolean paid; // ✅ KEY FIELD

    
}
