package com.nutritrack.controller;

import com.nutritrack.dto.FoodCreateRequest;
import com.nutritrack.dto.FoodResponse;
import com.nutritrack.dto.FoodUpdateRequest;
import com.nutritrack.dto.SimpleFoodDTO;
import com.nutritrack.service.FoodService;
import com.nutritrack.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/user")
    public ResponseEntity<List<FoodResponse>> getFoodsByUser() {
        Long userId = securityUtil.getUserIdFromToken();
        List<FoodResponse> foods = foodService.getFoodsByUserId(userId);
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable Long id) {
        FoodResponse foodResponse = foodService.getFoodById(id);
        return ResponseEntity.ok(foodResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodById(@PathVariable Long id) {
        foodService.deleteFoodById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody FoodCreateRequest foodRequest) {
        Long userId = securityUtil.getUserIdFromToken();
        FoodResponse createdFood = foodService.createFood(userId, foodRequest);
        return ResponseEntity.ok(createdFood);
    }

    @GetMapping("/user/simple")
    public ResponseEntity<List<SimpleFoodDTO>> getSimpleFoodsByUser() {
        Long userId = securityUtil.getUserIdFromToken();
        List<SimpleFoodDTO> foods = foodService.getSimpleFoodsByUserId(userId);
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/user/simple/search")
    public ResponseEntity<List<SimpleFoodDTO>> searchFoodsByTitle(@RequestParam String title) {
        Long userId = securityUtil.getUserIdFromToken();
        List<SimpleFoodDTO> foods = foodService.searchFoodsByTitle(userId, title);
        return ResponseEntity.ok(foods);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponse> updateFood(@PathVariable Long id, @RequestBody FoodUpdateRequest foodRequest) {
        Long userId = securityUtil.getUserIdFromToken();
        FoodResponse updatedFood = foodService.updateFood(id, foodRequest, userId);
        return ResponseEntity.ok(updatedFood);
    }
}
