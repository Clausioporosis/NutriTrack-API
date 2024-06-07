package com.nutritrack.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Schema(description = "User entity representing a user of the application")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Username of the user, must be unique")
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Email of the user, used for login and notifications")
    private String email;

    @Column(nullable = false, length = 255)
    @Schema(description = "Password of the user, should be stored in a hashed format")
    private String password;

    @Column(nullable = false, length = 50)
    @Schema(description = "First name of the user")
    private String firstName;

    @Column(nullable = false, length = 50)
    @Schema(description = "Last name of the user")
    private String lastName;

    @OneToMany(mappedBy = "user")
    @Schema(description = "List of foods created by the user")
    private List<Food> foods;

    @OneToMany(mappedBy = "user")
    @Schema(description = "List of trackings associated with the user")
    private List<Tracking> trackings;
}
