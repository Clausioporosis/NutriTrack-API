package com.nutritrack.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "Food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 100)
    private String brand;

    @Column(length = 50)
    private String category;
}
