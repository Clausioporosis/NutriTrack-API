package com.nutritrack.service;

import com.nutritrack.dto.FoodCreateRequest;
import com.nutritrack.dto.FoodResponse;
import com.nutritrack.dto.FoodUpdateRequest;
import com.nutritrack.dto.SimpleFoodDTO;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.model.*;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.mapper.FoodMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodMapper foodMapper;

    public List<FoodResponse> getFoodsByUserId(Long userId) {
        return foodRepository.findByUserId(userId).stream()
                .map(foodMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FoodResponse createFood(Long userId, FoodCreateRequest foodCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Food food = foodMapper.toEntity(foodCreateRequest, user);
        Food savedFood = foodRepository.save(food);
        return foodMapper.toResponse(savedFood);
    }

    @Transactional
    public void deleteFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodId));
        foodRepository.delete(food);
    }

    public FoodResponse getFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodId));
        return foodMapper.toResponse(food);
    }

    public List<SimpleFoodDTO> getSimpleFoodsByUserId(Long userId) {
        List<Food> foods = foodRepository.findByUserId(userId);
        return foods.stream()
                .map(food -> new SimpleFoodDTO(food.getId(), food.getTitle(), food.getBrand(), food.getCategory()))
                .collect(Collectors.toList());
    }

    public List<SimpleFoodDTO> searchFoodsByTitle(Long userId, String title) {
        List<Food> foods = foodRepository.findByUserIdAndTitleContaining(userId, title);
        return foods.stream()
                .map(food -> new SimpleFoodDTO(food.getId(), food.getTitle(), food.getBrand(), food.getCategory()))
                .collect(Collectors.toList());
    }

    @Transactional
    public FoodResponse updateFood(Long foodId, FoodUpdateRequest foodUpdateRequest, Long userId) {
        Food existingFood = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Map the basic fields
        existingFood.setTitle(foodUpdateRequest.getTitle());
        existingFood.setBrand(foodUpdateRequest.getBrand());
        existingFood.setCategory(foodUpdateRequest.getCategory());

        // Update nutrition information
        Nutrition nutrition = existingFood.getNutrition();
        if (nutrition == null) {
            nutrition = new Nutrition();
            existingFood.setNutrition(nutrition);
        }
        nutrition.setCalories(foodUpdateRequest.getNutrition().getCalories());
        nutrition.setProtein(foodUpdateRequest.getNutrition().getProtein());
        nutrition.setCarbs(foodUpdateRequest.getNutrition().getCarbs());
        nutrition.setFat(foodUpdateRequest.getNutrition().getFat());
        nutrition.setFood(existingFood);

        // Update sustainability information
        Sustainability sustainability = existingFood.getSustainability();
        if (sustainability == null) {
            sustainability = new Sustainability();
            existingFood.setSustainability(sustainability);
        }
        sustainability.setCo2perKg(foodUpdateRequest.getSustainability().getCo2perKg());
        sustainability.setDietType(foodUpdateRequest.getSustainability().getDietType());
        sustainability.setFood(existingFood);

        // Remove portions not in the request
        List<Long> portionIdsInRequest = foodUpdateRequest.getPortions().stream()
                .filter(portionRequest -> portionRequest.getId() != null)
                .map(FoodUpdateRequest.PortionUpdateRequest::getId)
                .collect(Collectors.toList());

        List<Portion> portionsToRemove = existingFood.getPortions().stream()
                .filter(portion -> portion.getId() != null && !portionIdsInRequest.contains(portion.getId()))
                .collect(Collectors.toList());

        existingFood.getPortions().removeAll(portionsToRemove);
        portionsToRemove.forEach(portionRepository::delete);

        // Update or add portions
        for (FoodUpdateRequest.PortionUpdateRequest portionRequest : foodUpdateRequest.getPortions()) {
            Portion portion;
            if (portionRequest.getId() != null) {
                portion = existingFood.getPortions().stream()
                        .filter(p -> p.getId().equals(portionRequest.getId()))
                        .findFirst()
                        .orElse(new Portion());
            } else {
                portion = new Portion();
            }

            portion.setLabel(portionRequest.getLabel());
            portion.setQuantity(portionRequest.getQuantity());
            portion.setFood(existingFood);

            if (portion.getId() == null) {
                existingFood.getPortions().add(portion);
            }
        }

        // Set the user reference
        existingFood.setUser(user);

        Food savedFood = foodRepository.save(existingFood);
        return foodMapper.toResponse(savedFood);
    }
}
