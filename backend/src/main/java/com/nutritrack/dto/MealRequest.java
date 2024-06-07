package com.nutritrack.dto;

import com.nutritrack.model.Food;
import com.nutritrack.model.Portion;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Sustainability;

import lombok.Data;

@Data
public class MealRequest {
    private Food food;
    private Portion portion;
    private Nutrition nutrition;
    private Sustainability sustainability;
}