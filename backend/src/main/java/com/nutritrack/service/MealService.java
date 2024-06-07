package com.nutritrack.service;

import org.springframework.stereotype.Service;
import com.nutritrack.model.*;
import com.nutritrack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

@Service
public class MealService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private SustainabilityRepository sustainabilityRepository;

    // getting 200, but still not sure if it's correct
    @Transactional
    public Food createFoodWithDetails(Food food, Portion portion, Nutrition nutrition, Sustainability sustainability,
            Long userId) {
        User user = new User();
        user.setId(userId);
        food.setUser(user);
        Food savedFood = foodRepository.save(food);

        portion.setFood(savedFood);
        portionRepository.save(portion);

        nutrition.setFood(savedFood);
        nutritionRepository.save(nutrition);

        sustainability.setFood(savedFood);
        sustainabilityRepository.save(sustainability);

        return savedFood;
    }
}