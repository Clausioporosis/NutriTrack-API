package com.nutritrack.mapper;

import com.nutritrack.dto.FoodCreateRequest;
import com.nutritrack.dto.FoodUpdateRequest;
import com.nutritrack.dto.FoodResponse;
import com.nutritrack.dto.FoodCreateRequest.NutritionRequest;
import com.nutritrack.dto.FoodCreateRequest.SustainabilityRequest;
import com.nutritrack.dto.FoodCreateRequest.PortionCreateRequest;
import com.nutritrack.dto.FoodUpdateRequest.PortionUpdateRequest;
import com.nutritrack.dto.FoodResponse.NutritionResponse;
import com.nutritrack.dto.FoodResponse.SustainabilityResponse;
import com.nutritrack.dto.FoodResponse.PortionResponse;
import com.nutritrack.model.*;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FoodMapper {

    // Method for creating Food
    public Food toEntity(FoodCreateRequest foodCreateRequest, User user) {
        Food food = new Food();
        food.setTitle(foodCreateRequest.getTitle());
        food.setBrand(foodCreateRequest.getBrand());
        food.setCategory(foodCreateRequest.getCategory());

        // Create nutrition information
        Nutrition nutrition = new Nutrition();
        nutrition.setCalories(foodCreateRequest.getNutrition().getCalories());
        nutrition.setProtein(foodCreateRequest.getNutrition().getProtein());
        nutrition.setCarbs(foodCreateRequest.getNutrition().getCarbs());
        nutrition.setFat(foodCreateRequest.getNutrition().getFat());
        food.setNutrition(nutrition);
        nutrition.setFood(food);

        // Create sustainability information
        Sustainability sustainability = new Sustainability();
        sustainability.setCo2perKg(foodCreateRequest.getSustainability().getCo2perKg());
        sustainability.setDietType(foodCreateRequest.getSustainability().getDietType());
        food.setSustainability(sustainability);
        sustainability.setFood(food);

        // Create portions
        food.setPortions(foodCreateRequest.getPortions().stream().map(portionRequest -> {
            Portion portion = new Portion();
            portion.setLabel(portionRequest.getLabel());
            portion.setQuantity(portionRequest.getQuantity());
            portion.setFood(food);
            return portion;
        }).collect(Collectors.toList()));

        // Set the user reference
        food.setUser(user);

        return food;
    }

    // Method for updating Food
    public Food toEntity(FoodUpdateRequest foodUpdateRequest, User user, Food existingFood) {
        existingFood.setTitle(foodUpdateRequest.getTitle());
        existingFood.setBrand(foodUpdateRequest.getBrand());
        existingFood.setCategory(foodUpdateRequest.getCategory());

        // Update nutrition information
        Nutrition nutrition = existingFood.getNutrition();
        if (nutrition == null) {
            nutrition = new Nutrition();
        }
        nutrition.setCalories(foodUpdateRequest.getNutrition().getCalories());
        nutrition.setProtein(foodUpdateRequest.getNutrition().getProtein());
        nutrition.setCarbs(foodUpdateRequest.getNutrition().getCarbs());
        nutrition.setFat(foodUpdateRequest.getNutrition().getFat());
        existingFood.setNutrition(nutrition);
        nutrition.setFood(existingFood);

        // Update sustainability information
        Sustainability sustainability = existingFood.getSustainability();
        if (sustainability == null) {
            sustainability = new Sustainability();
        }
        sustainability.setCo2perKg(foodUpdateRequest.getSustainability().getCo2perKg());
        sustainability.setDietType(foodUpdateRequest.getSustainability().getDietType());
        existingFood.setSustainability(sustainability);
        sustainability.setFood(existingFood);

        // Update or add portions
        foodUpdateRequest.getPortions().forEach(portionRequest -> {
            Portion portion = existingFood.getPortions().stream()
                    .filter(p -> portionRequest.getId() != null && p.getId().equals(portionRequest.getId()))
                    .findFirst()
                    .orElse(new Portion());

            portion.setLabel(portionRequest.getLabel());
            portion.setQuantity(portionRequest.getQuantity());
            portion.setFood(existingFood);

            if (portion.getId() == null) {
                existingFood.getPortions().add(portion);
            }
        });

        // Set the user reference
        existingFood.setUser(user);

        return existingFood;
    }

    // Method to convert Food entity to FoodResponse DTO
    public FoodResponse toResponse(Food food) {
        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setTitle(food.getTitle());
        foodResponse.setBrand(food.getBrand());
        foodResponse.setCategory(food.getCategory());

        // Map nutrition information
        NutritionResponse nutritionResponse = new NutritionResponse();
        Nutrition nutrition = food.getNutrition();
        if (nutrition != null) {
            nutritionResponse.setCalories(nutrition.getCalories());
            nutritionResponse.setProtein(nutrition.getProtein());
            nutritionResponse.setCarbs(nutrition.getCarbs());
            nutritionResponse.setFat(nutrition.getFat());
        }
        foodResponse.setNutrition(nutritionResponse);

        // Map sustainability information
        SustainabilityResponse sustainabilityResponse = new SustainabilityResponse();
        Sustainability sustainability = food.getSustainability();
        if (sustainability != null) {
            sustainabilityResponse.setCo2perKg(sustainability.getCo2perKg());
            sustainabilityResponse.setDietType(sustainability.getDietType());
        }
        foodResponse.setSustainability(sustainabilityResponse);

        // Map portions
        foodResponse.setPortions(food.getPortions().stream().map(portion -> {
            PortionResponse portionResponse = new PortionResponse();
            portionResponse.setId(portion.getId());
            portionResponse.setLabel(portion.getLabel());
            portionResponse.setQuantity(portion.getQuantity());
            return portionResponse;
        }).collect(Collectors.toList()));

        return foodResponse;
    }
}
