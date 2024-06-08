package com.nutritrack.dto;

import lombok.Data;
import java.util.List;

@Data
public class FoodResponse {
    private String title;
    private String brand;
    private String category;
    private NutritionResponse nutrition;
    private SustainabilityResponse sustainability;
    private List<PortionResponse> portions;

    @Data
    public static class NutritionResponse {
        private double calories;
        private double protein;
        private double carbs;
        private double fat;
    }

    @Data
    public static class SustainabilityResponse {
        private double co2perKg;
        private String dietType;
    }

    @Data
    public static class PortionResponse {
        private Long id;
        private String label;
        private double quantity;
    }
}
