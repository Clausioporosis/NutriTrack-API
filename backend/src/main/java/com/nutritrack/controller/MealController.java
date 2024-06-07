package com.nutritrack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Portion;
import com.nutritrack.model.Sustainability;
import com.nutritrack.service.MealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nutritrack.dto.MealRequest;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @PostMapping()
    public ResponseEntity<Food> createMeal(@RequestBody MealRequest mealRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserIdFromToken(); // Implement this method to extract userId from JWT

        Food food = mealRequest.getFood();
        Portion portion = mealRequest.getPortion();
        Nutrition nutrition = mealRequest.getNutrition();
        Sustainability sustainability = mealRequest.getSustainability();

        Food createdFood = mealService.createFoodWithDetails(food, portion, nutrition, sustainability, userId);

        return ResponseEntity.ok(createdFood);
    }

    private Long getUserIdFromToken() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        String userIdString = (String) authentication.getToken().getClaims().get("sub");
        return Long.valueOf(userIdString);
    }
}