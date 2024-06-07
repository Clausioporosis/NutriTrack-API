package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@Entity
@Table(name = "portions", indexes = {
        @Index(name = "idx_food_id", columnList = "food_id")
})
@Schema(description = "Portion entity representing a portion of a food item")
public class Portion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the portion")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "Label of the portion, for example, 'Slice' or 'Cup'")
    private String label;

    @Column(nullable = false)
    @Schema(description = "Quantity of the portion in grams or milliliters")
    private float quantity;

    @Column(nullable = false)
    @Schema(description = "Flag indicating if the portion is active")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    @Schema(description = "Food item associated with the portion")
    private Food food;

    @OneToMany(mappedBy = "portion")
    @Schema(description = "List of trackings associated with the portion")
    private List<Tracking> trackings;
}
