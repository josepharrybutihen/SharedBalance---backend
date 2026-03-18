package com.sharedbalance.sharedbalancebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean pushNotifications;

    private String language;
}