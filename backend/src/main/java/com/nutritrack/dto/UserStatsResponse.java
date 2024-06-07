package com.nutritrack.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserStatsResponse {
    private Long userId;
    private Date date;
    private Double savedCo2;
    private Integer vegetarianMeals;
    private Integer veganMeals;
}
