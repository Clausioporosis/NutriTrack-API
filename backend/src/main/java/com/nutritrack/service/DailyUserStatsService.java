package com.nutritrack.service;

import com.nutritrack.dto.DailyTrackingSummary;
import com.nutritrack.model.DailyUserStats;
import com.nutritrack.model.DietType;
import com.nutritrack.model.Food;
import com.nutritrack.model.Tracking;
import com.nutritrack.repository.DailyUserStatsRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.repository.TrackingRepository;
import com.nutritrack.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailyUserStatsService {

    @Autowired
    private DailyUserStatsRepository dailyUserStatsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    @Transactional
    public void saveDailyStats(Long userId, LocalDate date) {
        List<Tracking> trackings = trackingRepository.findByUserIdAndTimestampBetween(userId, date.atStartOfDay(),
                date.atTime(LocalTime.MAX));
        DailyTrackingSummary.DailySummary dailySummary = calculateDailySummary(trackings);

        DailyUserStats stats = dailyUserStatsRepository.findByUserIdAndDate(userId, date)
                .orElse(new DailyUserStats());

        stats.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)));
        stats.setDate(date);
        stats.setTotalCalories(dailySummary.getTotalCalories());
        stats.setTotalProtein(dailySummary.getTotalProtein());
        stats.setTotalCarbs(dailySummary.getTotalCarbs());
        stats.setTotalFat(dailySummary.getTotalFat());
        stats.setDailyCo2Emissions(dailySummary.getTotalCo2());
        stats.setDailyVeganMeals(dailySummary.getTotalVeganMeals());
        stats.setDailyVegetarianMeals(dailySummary.getTotalVegetarianMeals());
        stats.setDailyPoints(dailySummary.getDailyPoints());

        dailyUserStatsRepository.save(stats);
    }

    @Transactional(readOnly = true)
    public DailyTrackingSummary.DailySummary getDailySummary(Long userId, LocalDate date) {
        DailyUserStats stats = dailyUserStatsRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stats not found for user id: " + userId + " on date: " + date));

        DailyTrackingSummary.DailySummary dailySummary = new DailyTrackingSummary.DailySummary();
        dailySummary.setTotalCalories(stats.getTotalCalories());
        dailySummary.setTotalProtein(stats.getTotalProtein());
        dailySummary.setTotalCarbs(stats.getTotalCarbs());
        dailySummary.setTotalFat(stats.getTotalFat());
        dailySummary.setTotalCo2(stats.getDailyCo2Emissions());
        dailySummary.setTotalVeganMeals(stats.getDailyVeganMeals());
        dailySummary.setTotalVegetarianMeals(stats.getDailyVegetarianMeals());
        dailySummary.setDailyPoints(stats.getDailyPoints());

        return dailySummary;
    }

    public DailyTrackingSummary.DailySummary calculateDailySummary(List<Tracking> trackings) {
        DailyTrackingSummary.DailySummary dailySummary = new DailyTrackingSummary.DailySummary();
        for (Tracking tracking : trackings) {
            Food food = tracking.getFood();

            if (food == null) {
                continue;
            }

            float portionQuantity;
            if (tracking.getPortion() != null) {
                portionQuantity = tracking.getPortion().getQuantity() * tracking.getQuantity();
            } else {
                portionQuantity = tracking.getQuantity();
            }

            float portionMultiplier = portionQuantity / 100;

            // Berechnungen der Nährwerte und CO2-Emissionen basierend auf der Gesamtmenge
            dailySummary.setTotalCalories(
                    dailySummary.getTotalCalories() + (food.getNutrition().getCalories() * portionMultiplier));
            dailySummary.setTotalProtein(
                    dailySummary.getTotalProtein() + (food.getNutrition().getProtein() * portionMultiplier));
            dailySummary.setTotalCarbs(
                    dailySummary.getTotalCarbs() + (food.getNutrition().getCarbs() * portionMultiplier));
            dailySummary.setTotalFat(
                    dailySummary.getTotalFat() + (food.getNutrition().getFat() * portionMultiplier));
            float totalCo2ForEntry = food.getSustainability().getCo2perKg() * portionQuantity / 1000;
            dailySummary.setTotalCo2(dailySummary.getTotalCo2() + totalCo2ForEntry);

            // Punkteverteilung basierend auf Ernährungstyp
            if (food.getSustainability().getDietType() == DietType.VEGAN) {
                dailySummary.setTotalVeganMeals(dailySummary.getTotalVeganMeals() + 1);
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 15);
            } else if (food.getSustainability().getDietType() == DietType.VEGETARIAN) {
                dailySummary.setTotalVegetarianMeals(dailySummary.getTotalVegetarianMeals() + 1);
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 8);
            } else {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 2);
            }

            // Punkteverteilung basierend auf CO2-Emissionen
            if (totalCo2ForEntry < 0.1) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 10);
            } else if (totalCo2ForEntry < 0.2) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 5);
            } else if (totalCo2ForEntry < 0.3) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 2);
            }

            // Punkteverteilung basierend auf Kaloriengehalt
            if (food.getNutrition().getCalories() < 100) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 5);
            } else if (food.getNutrition().getCalories() < 200) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 3);
            } else if (food.getNutrition().getCalories() < 300) {
                dailySummary.setDailyPoints(dailySummary.getDailyPoints() + 1);
            }
        }
        return dailySummary;
    }
}
