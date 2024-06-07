package com.nutritrack.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DailyIntakeResponse {
    private Long intakeId;
    private Long foodId;
    private Long portionId;
    private Date date;
    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double quantity;
}
