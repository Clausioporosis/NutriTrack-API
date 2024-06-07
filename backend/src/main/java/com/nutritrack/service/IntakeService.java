package com.nutritrack.service;

import com.nutritrack.dto.DailyIntakeRequest;
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
    public DailyIntake trackFoodIntake(DailyIntakeRequest request) throws ParseException {
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

        Date date = new Date(DATE_FORMAT.parse(request.getDate()).getTime()); // Konvertierung von String zu
                                                                              // java.sql.Date

        DailyIntake dailyIntake = new DailyIntake();
        dailyIntake.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        dailyIntake.setFood(food);
        dailyIntake.setPortion(portionRepository.findById(request.getPortionId()).orElse(null));
        dailyIntake.setDate(date);
        dailyIntake.setCalories(calories);
        dailyIntake.setProtein(protein);
        dailyIntake.setCarbohydrates(carbohydrates);
        dailyIntake.setFat(fat);
        dailyIntake.setQuantity(request.getQuantity());

        dailyIntake = dailyIntakeRepository.save(dailyIntake);

        userStatsService.updateUserStats(userId, date, dailyIntake, false);

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

        userStatsService.updateUserStats(intake.getUser().getId(), intake.getDate(), intake, false);

        return intake;
    }

    @Transactional
    public void deleteFoodIntake(Long intakeId) {
        DailyIntake intake = dailyIntakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        dailyIntakeRepository.delete(intake);

        userStatsService.updateUserStats(intake.getUser().getId(), intake.getDate(), intake, true);
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
        List<DailyIntake> intakes = dailyIntakeRepository.findByUserIdAndDate(userId, date);
        return intakes.stream()
                .map(this::toBasicDailyIntakeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetailedDailyIntakeResponse getIntakeById(Long intakeId) {
        DailyIntake intake = dailyIntakeRepository.findById(intakeId)
                .orElseThrow(() -> new RuntimeException("Intake not found"));

        return toDetailedDailyIntakeResponse(intake);
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

    private DetailedDailyIntakeResponse toDetailedDailyIntakeResponse(DailyIntake dailyIntake) {
        DetailedDailyIntakeResponse response = new DetailedDailyIntakeResponse();
        response.setIntakeId(dailyIntake.getIntakeId());
        response.setFoodId(dailyIntake.getFood().getId());
        response.setTitle(dailyIntake.getFood().getTitle());
        response.setBrand(dailyIntake.getFood().getBrand());
        response.setCategory(dailyIntake.getFood().getCategory());

        Nutrition nutrition = nutritionRepository.findByFoodId(dailyIntake.getFood().getId());
        if (nutrition != null) {
            response.setIsLiquid(nutrition.getIsLiquid());
            response.setCalories(nutrition.getCalories());
            response.setProtein(nutrition.getProtein());
            response.setCarbohydrates(nutrition.getCarbohydrates());
            response.setFat(nutrition.getFat());
        }

        Sustainability sustainability = sustainabilityRepository.findByFoodId(dailyIntake.getFood().getId());
        if (sustainability != null) {
            response.setCo2Footprint(sustainability.getCo2Footprint());
            response.setVeganOrVegetarian(sustainability.getVeganOrVegetarian());
        }

        if (dailyIntake.getPortion() != null) {
            response.setPortionId(dailyIntake.getPortion().getPortionId());
            response.setPortionLabel(dailyIntake.getPortion().getPortionLabel());
            response.setAmountPerPortion(dailyIntake.getPortion().getAmountPerPortion());
        }
        response.setQuantity(dailyIntake.getQuantity());

        return response;
    }

    private void updateUserStats(Long userId, Date date, DailyIntake dailyIntake) {
        updateUserStats(userId, new java.sql.Date(date.getTime()), dailyIntake, false);
    }

    private void updateUserStats(Long userId, java.sql.Date date, DailyIntake dailyIntake, boolean isDelete) {
        userStatsService.updateUserStats(userId, date, dailyIntake, isDelete);
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
