package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@Entity
@Table(name = "foods", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Schema(description = "Food entity representing a food item")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the food")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Title of the food")
    private String title;

    @Column(nullable = false, length = 100)
    @Schema(description = "Branch of the food, for example, 'Fruit' or 'Vegetable'")
    private String branch;

    @Column(nullable = false, length = 100)
    @Schema(description = "Category of the food, for example, 'Snack' or 'Meal'")
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Diet type of the food, such as 'VEGAN', 'VEGETARIAN', or 'OMNIVORE'")
    private DietType dietType;

    @Column(nullable = false)
    @Schema(description = "CO2 emission per kg of the food, in kilograms")
    private float co2PerKg;

    @Column(nullable = false)
    @Schema(description = "Flag indicating if the food is active")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who created the food")
    private User user;

    @OneToMany(mappedBy = "food")
    @Schema(description = "List of portions associated with the food")
    private List<Portion> portions;

    @OneToMany(mappedBy = "food")
    @Schema(description = "List of trackings associated with the food")
    private List<Tracking> trackings;

    @OneToOne(mappedBy = "food", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Nutrition information of the food")
    private Nutrition nutrition;
}
