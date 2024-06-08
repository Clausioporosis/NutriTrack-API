package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "nutrition")
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double calories;
    private double protein;
    private double carbs;
    private double fat;

    @OneToOne(mappedBy = "nutrition", fetch = FetchType.LAZY)
    @JsonBackReference
    private Food food;
}
