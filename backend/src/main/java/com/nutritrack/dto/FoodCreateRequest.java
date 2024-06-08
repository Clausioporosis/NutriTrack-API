package com.nutritrack.dto;

import lombok.Data;
import java.util.List;

@Data
public class FoodCreateRequest {
    private String title;
    private String brand;
    private String category;
    private NutritionRequest nutrition;
    private SustainabilityRequest sustainability;
    private List<PortionCreateRequest> portions;

    @Data
    public static class NutritionRequest {
        private double calories;
        private double protein;
        private double carbs;
        private double fat;
    }

    @Data
    public static class SustainabilityRequest {
        private double co2perKg;
        private String dietType;
    }

    @Data
    public static class PortionCreateRequest {
        private String label;
        private double quantity;
    }
}
