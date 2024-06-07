package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "Portion")
public class Portion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portionId;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    private Food food;

    @Column(length = 50)
    private String portionLabel;

    private Double amountPerPortion;
}
