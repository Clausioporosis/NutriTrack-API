package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "User_Stats")
@IdClass(UserStatsId.class)
public class UserStats {
    @Id
    @Column(nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Double savedCo2;

    @Column(nullable = false)
    private Integer vegetarianMeals;

    @Column(nullable = false)
    private Integer veganMeals;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
}