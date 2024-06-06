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
@Table(name = "portion")
public class Portion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portion_id")
    private Long portionId;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "portion_label", length = 50)
    private String portionLabel;

    @Column(name = "amount_per_portion")
    private Double amountPerPortion;
}
