package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request data for portions")
public class PortionRequest {
    private Long id;
    @NotBlank
    private String label;
    @NotNull
    private int quantity;
}
