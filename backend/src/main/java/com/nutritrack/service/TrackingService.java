package com.nutritrack.service;

import com.nutritrack.dto.DailyTrackingSummary;
import com.nutritrack.dto.TrackingCreateRequest;
import com.nutritrack.dto.TrackingResponse;
import com.nutritrack.dto.TrackingUpdateRequest;
import com.nutritrack.mapper.TrackingMapper;
import com.nutritrack.model.DietType;
import com.nutritrack.model.Food;
import com.nutritrack.model.Portion;
import com.nutritrack.model.Tracking;
import com.nutritrack.model.User;
import com.nutritrack.repository.FoodRepository;
import com.nutritrack.repository.PortionRepository;
import com.nutritrack.repository.TrackingRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private PortionRepository portionRepository;

    @Autowired
    private TrackingMapper trackingMapper;

    @Transactional
    public TrackingResponse createTracking(Long userId, TrackingCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + request.getFoodId()));

        Portion portion = Optional.ofNullable(request.getPortionId())
                .map(portionId -> portionRepository.findById(portionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Portion not found with id: " + portionId)))
                .orElse(null);

        Tracking tracking = new Tracking();
        tracking.setUser(user);
        tracking.setFood(food);
        tracking.setPortion(portion);
        tracking.setQuantity(request.getQuantity());
        tracking.setTimestamp(LocalDateTime.now());

        Tracking savedTracking = trackingRepository.save(tracking);
        return trackingMapper.toResponse(savedTracking);
    }

    @Transactional(readOnly = true)
    public List<Tracking> getTrackingsByUserId(Long userId) {
        return trackingRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteTracking(Long trackingId) {
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking entry not found with id: " + trackingId));
        trackingRepository.delete(tracking);
    }

    @Transactional
    public TrackingResponse updateTracking(Long trackingId, TrackingUpdateRequest request) {
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking entry not found with id: " + trackingId));

        Portion portion = Optional.ofNullable(request.getPortionId())
                .map(portionId -> portionRepository.findById(portionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Portion not found with id: " + portionId)))
                .orElse(null);

        tracking.setPortion(portion);
        tracking.setQuantity(request.getQuantity());

        Tracking updatedTracking = trackingRepository.save(tracking);
        return trackingMapper.toResponse(updatedTracking);
    }

    @Transactional(readOnly = true)
    public DailyTrackingSummary getTrackingsByUserAndDate(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Tracking> trackings = trackingRepository.findByUserIdAndTimestampBetween(userId, startOfDay, endOfDay);
        List<TrackingResponse> trackingResponses = trackings.stream()
                .map(trackingMapper::toResponse)
                .collect(Collectors.toList());

        DailyTrackingSummary summary = new DailyTrackingSummary();
        summary.setTrackings(trackingResponses);

        summary.setSummary(calculateDailySummary(trackings));
        return summary;
    }

    private DailyTrackingSummary.DailySummary calculateDailySummary(List<Tracking> trackings) {
        DailyTrackingSummary.DailySummary dailySummary = new DailyTrackingSummary.DailySummary();
        for (Tracking tracking : trackings) {
            Food food = tracking.getFood();

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
            dailySummary.setTotalCo2(
                    dailySummary.getTotalCo2() + (food.getSustainability().getCo2perKg() * portionQuantity / 1000));

            if (food.getSustainability().getDietType() == DietType.VEGAN) {
                dailySummary.setTotalVeganMeals(dailySummary.getTotalVeganMeals() + 1);
            } else if (food.getSustainability().getDietType() == DietType.VEGETARIAN) {
                dailySummary.setTotalVegetarianMeals(dailySummary.getTotalVegetarianMeals() + 1);
            }
        }
        return dailySummary;
    }
}
