package com.sharedbalance.sharedbalancebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payer;

    private Double totalAmount;

    private Integer participantCount;
}
