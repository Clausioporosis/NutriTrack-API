package com.nutritrack.dto;

import lombok.Data;

@Data
public class FoodResponse {
    private Long id;
    private String title;
    private String brand;
    private String category;
}
