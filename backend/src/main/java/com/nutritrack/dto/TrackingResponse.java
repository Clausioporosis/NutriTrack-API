package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Response DTO for tracking entry")
public class TrackingResponse {
    private Long id;
    private Long foodId;
    private String foodTitle;
    private String foodBrand;
    private String foodCategory;
    private boolean IsLiquid;
    private Long portionId;
    private String portionLabel;
    private double portionQuantity;
    private double quantity;
    private LocalDateTime timestamp;
}