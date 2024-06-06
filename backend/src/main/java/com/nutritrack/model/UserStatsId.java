package com.nutritrack.model;

import lombok.Data;
import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.*;

@Data
@Embeddable
public class UserStatsId implements Serializable {
    private Long userId;
    private Date date;
}
