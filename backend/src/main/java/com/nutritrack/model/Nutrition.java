package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Table(name = "nutritions", indexes = {
        @Index(name = "idx_food_id", columnList = "food_id")
})
@Schema(description = "Nutrition entity representing nutritional information of a food item")
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the nutrition information")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Flag indicating if the food is a liquid")
    private boolean isLiquid;

    @Column(nullable = false)
    @Schema(description = "Calories per 100g or 100ml of the food")
    private float calories;

    @Column(nullable = false)
    @Schema(description = "Protein content per 100g or 100ml of the food in grams")
    private float protein;

    @Column(nullable = false)
    @Schema(description = "Carbohydrate content per 100g or 100ml of the food in grams")
    private float carbs;

    @Column(nullable = false)
    @Schema(description = "Fat content per 100g or 100ml of the food in grams")
    private float fat;

    @OneToOne
    @JoinColumn(name = "food_id", nullable = false)
    @Schema(description = "Food item associated with the nutrition information")
    private Food food;
}
