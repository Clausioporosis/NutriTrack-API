package com.nutritrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.util.FoodMapper;
import com.nutritrack.util.SecurityUtil;

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

        Portion portion = new Portion();
        portion.setFood(food);
        portion.setPortionLabel(dto.getPortionLabel());
        portion.setAmountPerPortion(dto.getAmountPerPortion());
        portionRepository.save(portion);

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

        Portion portion = portionRepository.findByFoodId(foodId);
        portion.setPortionLabel(dto.getPortionLabel());
        portion.setAmountPerPortion(dto.getAmountPerPortion());
        portionRepository.save(portion);

        return food;
    }

    @Transactional(readOnly = true)
    public FullFoodResponse getFoodById(Long foodId, Long userId) {
        Food food = foodRepository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new RuntimeException("Food not found or does not belong to the user"));

        Nutrition nutrition = nutritionRepository.findByFoodId(food.getId());
        Sustainability sustainability = sustainabilityRepository.findByFoodId(food.getId());
        Portion portion = portionRepository.findByFoodId(food.getId());

        return FoodMapper.toFullFoodResponse(food, nutrition, sustainability, portion);
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
