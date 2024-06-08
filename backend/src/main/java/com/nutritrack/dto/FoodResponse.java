package com.nutritrack.dto;

import lombok.Data;
import java.util.List;

import com.nutritrack.model.DietType;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class FoodResponse {
    private Long id;
    private String title;
    private String brand;
    private String category;
    private boolean isLiquid;
    private NutritionResponse nutrition;
    private SustainabilityResponse sustainability;
    private List<PortionResponse> portions;
    private boolean deactivated;

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
        @Schema(example = "OMNIVORE|VEGETARIAN|VEGAN")
        private DietType dietType;
    }

    @Data
    public static class PortionResponse {
        private Long id;
        private String label;
        private double quantity;
    }
}