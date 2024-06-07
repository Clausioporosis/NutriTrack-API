package com.nutritrack.dto;

import lombok.Data;

@Data
public class PortionResponse {
    private Long portionId;
    private String portionLabel;
    private Double amountPerPortion;
}