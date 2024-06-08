package com.nutritrack.dto;

import lombok.Data;

@Data
public class PortionUpdateRequest {
    private Long id;
    private String label;
    private double quantity;
}
