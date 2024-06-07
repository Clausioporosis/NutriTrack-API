package com.nutritrack.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DetailedDailyIntakeResponse {
    private Long intakeId;
    private Long foodId;
    private String title;
    private String brand;
    private String category;
    private Boolean isLiquid;
    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double co2Footprint;
    private String veganOrVegetarian;
    private Long portionId;
    private String portionLabel;
    private Double amountPerPortion;
    private Double quantity;
}
