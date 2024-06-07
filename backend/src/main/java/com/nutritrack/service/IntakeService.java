package com.nutritrack.service;

import com.nutritrack.dto.DailyIntakeRequest;
import com.nutritrack.model.DailyIntake;
import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Portion;
import com.nutritrack.model.UserStats;
import com.nutritrack.model.UserStatsId;
import com.nutritrack.repository.DailyIntakeRepository;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.NutritionRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.repository.UserStatsRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.util.SecurityUtil;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntakeService {

    @Autowired
    private DailyIntakeRepository dailyIntakeRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Transactional
    public DailyIntake trackFoodIntake(DailyIntakeRequest request) {
        Long userId = SecurityUtil.getUserIdFromToken();
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        Nutrition nutrition = nutritionRepository.findByFoodId(food.getId());
        if (nutrition == null) {
            throw new RuntimeException("Nutrition information not found for food");
        }

        Double totalWeight;
        if (request.getPortionId() != null) {
            Portion portion = portionRepository.findById(request.getPortionId())
                    .orElseThrow(() -> new RuntimeException("Portion not found"));
            totalWeight = portion.getAmountPerPortion() * request.getQuantity();
        } else {
            totalWeight = request.getQuantity();
        }

        Double calories = (totalWeight / 100) * nutrition.getCalories();
        Double protein = (totalWeight / 100) * nutrition.getProtein();
        Double carbohydrates = (totalWeight / 100) * nutrition.getCarbohydrates();
        Double fat = (totalWeight / 100) * nutrition.getFat();

        DailyIntake dailyIntake = new DailyIntake();
        dailyIntake.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        dailyIntake.setFood(food);
        dailyIntake.setPortion(portionRepository.findById(request.getPortionId()).orElse(null));
        dailyIntake.setDate(new java.sql.Date(request.getDate().getTime())); // Konvertierung von java.util.Date zu
                                                                             // java.sql.Date
        dailyIntake.setCalories(calories);
        dailyIntake.setProtein(protein);
        dailyIntake.setCarbohydrates(carbohydrates);
        dailyIntake.setFat(fat);
        dailyIntake.setQuantity(request.getQuantity());

        dailyIntake = dailyIntakeRepository.save(dailyIntake);

        updateUserStats(userId, request.getDate(), dailyIntake);

        return dailyIntake;
    }

    private void updateUserStats(Long userId, Date date, DailyIntake dailyIntake) {
        UserStatsId userStatsId = new UserStatsId(userId, new java.sql.Date(date.getTime())); // Konvertierung von
                                                                                              // java.util.Date zu
                                                                                              // java.sql.Date

        UserStats userStats = userStatsRepository.findById(userStatsId)
                .orElse(new UserStats(userId, new java.sql.Date(date.getTime())));

        userStats.setSavedCo2(userStats.getSavedCo2() + dailyIntake.getCalories()); // Beispiel: CO2-Einsparung durch
                                                                                    // Kalorien
        userStats.setVegetarianMeals(userStats.getVegetarianMeals()
                + (dailyIntake.getFood().getCategory().equalsIgnoreCase("vegetarian") ? 1 : 0));
        userStats.setVeganMeals(
                userStats.getVeganMeals() + (dailyIntake.getFood().getCategory().equalsIgnoreCase("vegan") ? 1 : 0));

        userStatsRepository.save(userStats);
    }
}