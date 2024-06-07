package com.nutritrack.model;

import lombok.Data;
import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.*;

public class UserStatsId implements Serializable {
    private Long userId;
    private Date date;

    // Default constructor, equals, and hashCode methods
}