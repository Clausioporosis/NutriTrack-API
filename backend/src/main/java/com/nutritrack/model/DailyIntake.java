package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Daily_Intake")
public class DailyIntake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intakeId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    private Food food;

    @ManyToOne
    @JoinColumn(name = "portionId", nullable = true)
    private Portion portion;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Double calories;

    @Column(nullable = false)
    private Double protein;

    @Column(nullable = false)
    private Double carbohydrates;

    @Column(nullable = false)
    private Double fat;

    @Column(nullable = false)
    private Double quantity;
}