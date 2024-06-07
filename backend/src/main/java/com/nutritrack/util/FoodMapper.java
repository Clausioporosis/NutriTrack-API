package com.nutritrack.util;

import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Portion;
import com.nutritrack.model.Sustainability;
import com.nutritrack.dto.FoodResponse;
import com.nutritrack.dto.FullFoodResponse;
import com.nutritrack.dto.PortionResponse;
import java.util.List;
import java.util.stream.Collectors;

public class FoodMapper {

    public static FoodResponse toFoodResponse(Food food) {
        FoodResponse response = new FoodResponse();
        response.setId(food.getId());
        response.setTitle(food.getTitle());
        response.setBrand(food.getBrand());
        response.setCategory(food.getCategory());
        return response;
    }

    public static FullFoodResponse toFullFoodResponse(Food food, Nutrition nutrition, Sustainability sustainability,
            List<Portion> portions) {
        FullFoodResponse response = new FullFoodResponse();
        response.setId(food.getId());
        response.setTitle(food.getTitle());
        response.setBrand(food.getBrand());
        response.setCategory(food.getCategory());

        if (nutrition != null) {
            response.setIsLiquid(nutrition.getIsLiquid());
            response.setCalories(nutrition.getCalories());
            response.setProtein(nutrition.getProtein());
            response.setCarbohydrates(nutrition.getCarbohydrates());
            response.setFat(nutrition.getFat());
        }

        if (sustainability != null) {
            response.setCo2Footprint(sustainability.getCo2Footprint());
            response.setVeganOrVegetarian(sustainability.getVeganOrVegetarian());
        }

        if (portions != null) {
            List<PortionResponse> portionResponses = portions.stream()
                    .map(FoodMapper::toPortionResponse)
                    .collect(Collectors.toList());
            response.setPortions(portionResponses);
        }

        return response;
    }

    private static PortionResponse toPortionResponse(Portion portion) {
        PortionResponse response = new PortionResponse();
        response.setPortionId(portion.getPortionId());
        response.setPortionLabel(portion.getPortionLabel());
        response.setAmountPerPortion(portion.getAmountPerPortion());
        return response;
    }
}