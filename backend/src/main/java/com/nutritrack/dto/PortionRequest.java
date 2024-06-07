package com.nutritrack.dto;

import lombok.Data;

@Data
public class PortionRequest {
    private String portionLabel;
    private Double amountPerPortion;
}