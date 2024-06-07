package com.nutritrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nutritrack.dto.UserStatsResponse;
import com.nutritrack.model.UserStats;
import com.nutritrack.repository.UserStatsRepository;
import com.nutritrack.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import com.nutritrack.model.DailyIntake;
import com.nutritrack.model.UserStatsId;
import com.nutritrack.repository.SustainabilityRepository;
import com.nutritrack.model.Sustainability;

@Service
public class UserStatsService {

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Autowired
    private SustainabilityRepository sustainabilityRepository;

    @Transactional(readOnly = true)
    public List<UserStatsResponse> getUserStats() {
        Long userId = SecurityUtil.getUserIdFromToken();
        List<UserStats> userStatsList = userStatsRepository.findByUserId(userId);

        return userStatsList.stream()
                .map(this::toUserStatsResponse)
                .collect(Collectors.toList());
    }

    private UserStatsResponse toUserStatsResponse(UserStats userStats) {
        UserStatsResponse response = new UserStatsResponse();
        response.setUserId(userStats.getUserId());
        response.setDate(userStats.getDate());
        response.setSavedCo2(userStats.getSavedCo2());
        response.setVegetarianMeals(userStats.getVegetarianMeals());
        response.setVeganMeals(userStats.getVeganMeals());
        return response;
    }

    @Transactional
    public void resetUserStats() {
        Long userId = SecurityUtil.getUserIdFromToken();
        userStatsRepository.deleteByUserId(userId);
    }

    @Transactional
    public void updateUserStats(Long userId, java.sql.Date date, DailyIntake dailyIntake, boolean isDelete) {
        UserStatsId userStatsId = new UserStatsId(userId, date);
        UserStats userStats = userStatsRepository.findById(userStatsId)
                .orElse(new UserStats(userId, date));

        Sustainability sustainability = sustainabilityRepository.findByFoodId(dailyIntake.getFood().getId());
        if (sustainability == null) {
            throw new RuntimeException("Sustainability information not found for food");
        }

        double co2 = sustainability.getCo2Footprint();
        double totalWeight = (dailyIntake.getPortion() != null)
                ? dailyIntake.getPortion().getAmountPerPortion() * dailyIntake.getQuantity()
                : dailyIntake.getQuantity();
        double savedCo2 = (totalWeight / 1000) * co2;

        if (isDelete) {
            userStats.setSavedCo2(userStats.getSavedCo2() - savedCo2);
            userStats.setVegetarianMeals(userStats.getVegetarianMeals()
                    - ("vegetarian".equalsIgnoreCase(sustainability.getVeganOrVegetarian()) ? 1 : 0));
            userStats.setVeganMeals(userStats.getVeganMeals()
                    - ("vegan".equalsIgnoreCase(sustainability.getVeganOrVegetarian()) ? 1 : 0));
        } else {
            userStats.setSavedCo2(userStats.getSavedCo2() + savedCo2);
            userStats.setVegetarianMeals(userStats.getVegetarianMeals()
                    + ("vegetarian".equalsIgnoreCase(sustainability.getVeganOrVegetarian()) ? 1 : 0));
            userStats.setVeganMeals(userStats.getVeganMeals()
                    + ("vegan".equalsIgnoreCase(sustainability.getVeganOrVegetarian()) ? 1 : 0));
        }

        userStatsRepository.save(userStats);
    }
}
