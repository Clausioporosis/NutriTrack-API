package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "Sustainability")
public class Sustainability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sustainabilityId;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    private Food food;

    private Double co2Footprint;

    @Column(length = 50)
    private String veganOrVegetarian;
}