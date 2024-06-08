package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request data for sustainability information")
public class SustainabilityRequest {
    @NotNull
    private double co2perKg;
    @NotBlank
    private String dietType;
}
