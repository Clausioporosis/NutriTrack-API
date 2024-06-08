package com.nutritrack.dto;

import lombok.Data;
import java.util.List;

import com.nutritrack.model.DietType;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class FoodUpdateRequest {
    private String title;
    private String brand;
    private String category;
    private NutritionRequest nutrition;
    private SustainabilityRequest sustainability;
    private List<PortionUpdateRequest> portions;

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
        @Schema(example = "OMNIVORE|VEGETARIAN|VEGAN")
        private DietType dietType;
    }

    @Data
    public static class PortionUpdateRequest {
        private Long id;
        private String label;
        private double quantity;
    }
}
