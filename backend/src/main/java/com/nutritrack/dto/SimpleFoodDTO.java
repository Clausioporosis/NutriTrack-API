package com.nutritrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleFoodDTO {
    private Long id;
    private String title;
    private String brand;
    private String category;
}
