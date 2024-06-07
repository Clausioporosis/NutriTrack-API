package com.nutritrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Portion;
import com.nutritrack.model.Sustainability;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.NutritionRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.repository.SustainabilityRepository;

import com.nutritrack.dto.FullFoodRequest;
import com.nutritrack.dto.FullFoodResponse;
import com.nutritrack.dto.FoodResponse;
import com.nutritrack.service.FoodService;
import com.nutritrack.util.FoodMapper;
import com.nutritrack.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Food Management", description = "APIs for managing foods")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private SustainabilityRepository sustainabilityRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Operation(summary = "Create a new food with nutrition, sustainability and portion information")
    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody FullFoodRequest dto) {
        Food createdFood = foodService.createFood(dto);
        FoodResponse response = FoodMapper.toFoodResponse(createdFood);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all foods for the authenticated user")
    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoodsForUser() {
        Long userId = SecurityUtil.getUserIdFromToken();
        List<Food> foods = foodRepository.findByUserId(userId);
        List<FoodResponse> response = foods.stream()
                .map(FoodMapper::toFoodResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search foods by title")
    public ResponseEntity<List<FoodResponse>> searchFoods(@RequestParam String query) {
        Long userId = SecurityUtil.getUserIdFromToken();
        List<Food> foods = foodRepository.findByUserIdAndTitleContainingIgnoreCase(userId, query);
        List<FoodResponse> response = foods.stream()
                .map(FoodMapper::toFoodResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{foodId}")
    @Operation(summary = "Get full details of a food")
    public ResponseEntity<FullFoodResponse> getFoodById(@PathVariable Long foodId) {
        Long userId = SecurityUtil.getUserIdFromToken();
        FullFoodResponse response = foodService.getFoodById(foodId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{foodId}")
    @Operation(summary = "Update an existing food")
    public ResponseEntity<FullFoodResponse> updateFood(@PathVariable Long foodId, @RequestBody FullFoodRequest dto) {
        Long userId = SecurityUtil.getUserIdFromToken();
        Food updatedFood = foodService.updateFood(foodId, userId, dto);
        Nutrition nutrition = nutritionRepository.findByFoodId(updatedFood.getId());
        Sustainability sustainability = sustainabilityRepository.findByFoodId(updatedFood.getId());
        List<Portion> portions = portionRepository.findByFoodId(updatedFood.getId());
        FullFoodResponse response = FoodMapper.toFullFoodResponse(updatedFood, nutrition, sustainability, portions);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{foodId}")
    @Operation(summary = "Delete a food")
    public ResponseEntity<Void> deleteFood(@PathVariable Long foodId) {
        Long userId = SecurityUtil.getUserIdFromToken();
        foodService.deleteFood(foodId, userId);
        return ResponseEntity.noContent().build();
    }
}