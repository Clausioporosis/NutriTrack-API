package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request data for nutritional information")
public class NutritionRequest {
    @NotNull
    private int calories;
    @NotNull
    private int protein;
    @NotNull
    private int carbs;
    @NotNull
    private int fat;
}
