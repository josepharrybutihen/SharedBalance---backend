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
    private String participants;

    @Column(columnDefinition = "TEXT")
    private String breakdown; // JSON string

    private Long groupId;
    private String groupName;
}
