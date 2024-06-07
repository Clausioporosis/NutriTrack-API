package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.sql.Date;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Double savedCo2 = 0.0;

    @Column(nullable = false)
    private Integer vegetarianMeals = 0;

    @Column(nullable = false)
    private Integer veganMeals = 0;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    // Benutzerdefinierter Konstruktor
    public UserStats(Long userId, Date date) {
        this.userId = userId;
        this.date = date;
        this.savedCo2 = 0.0;
        this.vegetarianMeals = 0;
        this.veganMeals = 0;
    }
}