package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "Nutrition")
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutritionId;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    private Food food;

    @Column(nullable = false)
    private Boolean isLiquid;

    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
}