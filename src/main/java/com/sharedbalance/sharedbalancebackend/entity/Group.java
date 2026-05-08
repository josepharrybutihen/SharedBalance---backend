package com.sharedbalance.sharedbalancebackend.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ElementCollection
    private List<String> members;

    private String category;  // Category field (for grouping, e.g., "Beach", "Party", etc.)

    private String categoryImg;  // Category image (to store the image path or URL)
    private String creatorName;
    private String creatorEmail;
}
