package com.nutritrack.service;

import com.nutritrack.dto.DailyIntakeRequest;
import com.nutritrack.dto.DailyIntakeResponse;
import com.nutritrack.model.DailyIntake;
import com.nutritrack.model.Food;
import com.nutritrack.model.Nutrition;
import com.nutritrack.model.Portion;
import com.nutritrack.repository.DailyIntakeRepository;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.NutritionRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.repository.UserStatsRepository;
import com.nutritrack.util.SecurityUtil;
import com.nutritrack.dto.BasicDailyIntakeResponse;
import com.nutritrack.dto.DetailedDailyIntakeResponse;
import com.nutritrack.model.Sustainability;
import com.nutritrack.repository.SustainabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserStatsService userStatsService;

    @Autowired
    private SustainabilityRepository sustainabilityRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
        Portion portion = null;
        if (request.getPortionId() != null) {
            portion = portionRepository.findById(request.getPortionId())
                    .orElseThrow(() -> new RuntimeException("Portion not found"));
            totalWeight = portion.getAmountPerPortion() * request.getQuantity();
        } else {
            totalWeight = request.getQuantity();
        }

        Double calories = (totalWeight / 100) * nutrition.getCalories();
        Double protein = (totalWeight / 100) * nutrition.getProtein();
        Double carbohydrates = (totalWeight / 100) * nutrition.getCarbohydrates();
        Double fat = (totalWeight / 100) * nutrition.getFat();

        // Speichere den vollständigen Zeitstempel
        Date currentDate = new Date(System.currentTimeMillis());

        DailyIntake dailyIntake = new DailyIntake();
        dailyIntake.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        dailyIntake.setFood(food);
        dailyIntake.setPortion(portion);
        dailyIntake.setDate(currentDate);
        dailyIntake.setCalories(calories);
        dailyIntake.setProtein(protein);
        dailyIntake.setCarbohydrates(carbohydrates);
        dailyIntake.setFat(fat);
        dailyIntake.setQuantity(totalWeight);

        dailyIntake = dailyIntakeRepository.save(dailyIntake);

        userStatsService.updateUserStats(userId, currentDate, dailyIntake, false);

        return dailyIntake;
    }

    @Transactional
    public DailyIntake updateFoodIntake(Long intakeId, DailyIntakeRequest request) {
        DailyIntake intake = dailyIntakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

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
            intake.setPortion(portion);
        } else {
            totalWeight = request.getQuantity();
            intake.setPortion(null); // Setze Portion auf null
        }

        Double calories = (totalWeight / 100) * nutrition.getCalories();
        Double protein = (totalWeight / 100) * nutrition.getProtein();
        Double carbohydrates = (totalWeight / 100) * nutrition.getCarbohydrates();
        Double fat = (totalWeight / 100) * nutrition.getFat();

        intake.setFood(food);
        intake.setCalories(calories);
        intake.setProtein(protein);
        intake.setCarbohydrates(carbohydrates);
        intake.setFat(fat);
        intake.setQuantity(request.getQuantity());

        intake = dailyIntakeRepository.save(intake);

        userStatsService.updateUserStats(intake.getUser().getId(), new java.sql.Date(intake.getDate().getTime()),
                intake, false);

        return intake;
    }

    @Transactional
    public void deleteFoodIntake(Long intakeId) {
        DailyIntake intake = dailyIntakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        dailyIntakeRepository.delete(intake);

        userStatsService.updateUserStats(intake.getUser().getId(), new java.sql.Date(intake.getDate().getTime()),
                intake, true);
    }

    @Transactional(readOnly = true)
    public List<BasicDailyIntakeResponse> getIntakesForDate(String dateString) {
        Long userId = SecurityUtil.getUserIdFromToken();
        Date date;
        try {
            date = new java.sql.Date(DATE_FORMAT.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Expected yyyy-MM-dd.", e);
        }

        // Berechne den Start- und Endzeitpunkt des Tages
        Date startDate = Date.valueOf(dateString);
        Date endDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000) - 1);

        List<DailyIntake> intakes = dailyIntakeRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        return intakes.stream()
                .map(this::toBasicDailyIntakeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DailyIntakeResponse getIntakeById(Long intakeId) {
        DailyIntake intake = dailyIntakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        return toDailyIntakeResponse(intake);
    }

    private DailyIntakeResponse toDailyIntakeResponse(DailyIntake dailyIntake) {
        DailyIntakeResponse response = new DailyIntakeResponse();
        response.setIntakeId(dailyIntake.getIntakeId());
        response.setFoodId(dailyIntake.getFood().getId());
        response.setPortionId(dailyIntake.getPortion() != null ? dailyIntake.getPortion().getPortionId() : null);
        response.setDate(dailyIntake.getDate());
        response.setCalories(dailyIntake.getCalories());
        response.setProtein(dailyIntake.getProtein());
        response.setCarbohydrates(dailyIntake.getCarbohydrates());
        response.setFat(dailyIntake.getFat());
        response.setQuantity(dailyIntake.getQuantity());
        return response;
    }

    private BasicDailyIntakeResponse toBasicDailyIntakeResponse(DailyIntake dailyIntake) {
        BasicDailyIntakeResponse response = new BasicDailyIntakeResponse();
        response.setIntakeId(dailyIntake.getIntakeId());
        response.setFoodId(dailyIntake.getFood().getId());
        response.setTitle(dailyIntake.getFood().getTitle());
        response.setBrand(dailyIntake.getFood().getBrand());
        response.setCategory(dailyIntake.getFood().getCategory());
        response.setDate(dailyIntake.getDate());
        return response;
    }

    @Transactional(readOnly = true)
    public List<BasicDailyIntakeResponse> getAllIntakes() {
        Long userId = SecurityUtil.getUserIdFromToken();
        List<DailyIntake> intakes = dailyIntakeRepository.findByUserId(userId);
        return intakes.stream()
                .map(this::toBasicDailyIntakeResponse)
                .collect(Collectors.toList());
    }
}
