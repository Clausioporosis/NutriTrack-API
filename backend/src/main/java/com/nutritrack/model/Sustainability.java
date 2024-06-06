package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sustainability")
public class Sustainability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sustainability_id")
    private Long sustainabilityId;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "co2_footprint")
    private Double co2Footprint;

    @Column(name = "vegan_or_vegetarian", length = 50)
    private String veganOrVegetarian;
}
