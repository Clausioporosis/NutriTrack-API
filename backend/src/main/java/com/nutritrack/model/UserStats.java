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
@Table(name = "user_stats")
public class UserStats {

    @EmbeddedId
    private UserStatsId id;

    @Column(name = "saved_co2", nullable = false)
    private Double savedCo2;

    @Column(name = "vegetarian_meals", nullable = false)
    private Integer vegetarianMeals;

    @Column(name = "vegan_meals", nullable = false)
    private Integer veganMeals;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
