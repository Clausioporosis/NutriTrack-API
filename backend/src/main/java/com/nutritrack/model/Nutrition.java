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

    private int calories;
    private int protein;
    private int carbs;
    private int fat;

    @OneToOne(mappedBy = "nutrition", fetch = FetchType.LAZY)
    @JsonBackReference
    private Food food;
}