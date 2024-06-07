package com.nutritrack.dto;

import lombok.Data;

@Data
public class FullFoodResponse {
    private Long id;
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
    private String portionLabel;
    private Double amountPerPortion;
}