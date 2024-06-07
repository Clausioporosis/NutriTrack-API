package com.nutritrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nutritrack.model.DailyIntake;
import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Sustainability;
import com.nutritrack.model.Portion;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.NutritionRepository;
import com.nutritrack.repository.SustainabilityRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.dto.FullFoodRequest;
import com.nutritrack.dto.FullFoodResponse;
import com.nutritrack.dto.PortionRequest;
import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.util.FoodMapper;
import com.nutritrack.util.SecurityUtil;
import com.nutritrack.repository.DailyIntakeRepository;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private SustainabilityRepository sustainabilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private DailyIntakeRepository dailyIntakeRepository;

    @Transactional
    public Food createFood(FullFoodRequest dto) {
        Long userId = SecurityUtil.getUserIdFromToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Food food = new Food();
        food.setTitle(dto.getTitle());
        food.setBrand(dto.getBrand());
        food.setCategory(dto.getCategory());
        food.setUser(user);
        food = foodRepository.save(food);

        Nutrition nutrition = new Nutrition();
        nutrition.setFood(food);
        nutrition.setIsLiquid(dto.getIsLiquid());
        nutrition.setCalories(dto.getCalories());
        nutrition.setProtein(dto.getProtein());
        nutrition.setCarbohydrates(dto.getCarbohydrates());
        nutrition.setFat(dto.getFat());
        nutritionRepository.save(nutrition);

        Sustainability sustainability = new Sustainability();
        sustainability.setFood(food);
        sustainability.setCo2Footprint(dto.getCo2Footprint());
        sustainability.setVeganOrVegetarian(dto.getVeganOrVegetarian());
        sustainabilityRepository.save(sustainability);

        if (dto.getPortions() != null) {
            for (PortionRequest portionRequest : dto.getPortions()) {
                Portion portion = new Portion();
                portion.setFood(food);
                portion.setPortionLabel(portionRequest.getPortionLabel());
                portion.setAmountPerPortion(portionRequest.getAmountPerPortion());
                portionRepository.save(portion);
            }
        }

        return food;
    }

    @Transactional
    public Food updateFood(Long foodId, Long userId, FullFoodRequest dto) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new RuntimeException("Food not found or does not belong to the user"));

        food.setTitle(dto.getTitle());
        food.setBrand(dto.getBrand());
        food.setCategory(dto.getCategory());
        food = foodRepository.save(food);

        Nutrition nutrition = nutritionRepository.findByFoodId(foodId);
        nutrition.setIsLiquid(dto.getIsLiquid());
        nutrition.setCalories(dto.getCalories());
        nutrition.setProtein(dto.getProtein());
        nutrition.setCarbohydrates(dto.getCarbohydrates());
        nutrition.setFat(dto.getFat());
        nutritionRepository.save(nutrition);

        Sustainability sustainability = sustainabilityRepository.findByFoodId(foodId);
        sustainability.setCo2Footprint(dto.getCo2Footprint());
        sustainability.setVeganOrVegetarian(dto.getVeganOrVegetarian());
        sustainabilityRepository.save(sustainability);

        List<Portion> existingPortions = portionRepository.findByFoodId(foodId);
        portionRepository.deleteAll(existingPortions);

        if (dto.getPortions() != null) {
            for (PortionRequest portionRequest : dto.getPortions()) {
                Portion portion = new Portion();
                portion.setFood(food);
                portion.setPortionLabel(portionRequest.getPortionLabel());
                portion.setAmountPerPortion(portionRequest.getAmountPerPortion());
                portionRepository.save(portion);
            }
        }

        return food;
    }

    @Transactional(readOnly = true)
    public FullFoodResponse getFoodById(Long foodId, Long userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new RuntimeException("Food not found or does not belong to the user"));

        Nutrition nutrition = nutritionRepository.findByFoodId(food.getId());
        Sustainability sustainability = sustainabilityRepository.findByFoodId(food.getId());
        List<Portion> portions = portionRepository.findByFoodId(food.getId());

        return FoodMapper.toFullFoodResponse(food, nutrition, sustainability, portions);
    }

    @Transactional
    public void deleteFood(Long foodId, Long userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new RuntimeException("Food not found or does not belong to the user"));

        nutritionRepository.deleteByFoodId(food.getId());
        sustainabilityRepository.deleteByFoodId(food.getId());
        portionRepository.deleteByFoodId(food.getId());
        foodRepository.delete(food);
    }
}