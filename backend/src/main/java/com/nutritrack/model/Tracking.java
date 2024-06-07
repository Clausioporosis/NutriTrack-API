package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trackings", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_food_id", columnList = "food_id"),
        @Index(name = "idx_portion_id", columnList = "portion_id"),
        @Index(name = "idx_datetime", columnList = "datetime")
})
@Schema(description = "Tracking entity representing a food tracking entry")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the tracking entry")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who tracked the food")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    @Schema(description = "Food item being tracked")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "portion_id", nullable = true)
    @Schema(description = "Portion of the food item being tracked")
    private Portion portion;

    @Column(nullable = false)
    @Schema(description = "Date and time of the tracking entry")
    private LocalDateTime datetime;

    @Column(nullable = false)
    @Schema(description = "Quantity of the food item being tracked")
    private float quantity;
}
