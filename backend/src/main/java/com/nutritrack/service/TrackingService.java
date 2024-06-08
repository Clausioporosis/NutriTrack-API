package com.nutritrack.service;

import com.nutritrack.dto.TrackingCreateRequest;
import com.nutritrack.dto.TrackingResponse;
import com.nutritrack.dto.TrackingUpdateRequest;
import com.nutritrack.mapper.TrackingMapper;
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

import java.time.LocalDateTime;
import java.util.List;

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

        Portion portion = null;
        if (request.getPortionId() != null) {
            portion = portionRepository.findById(request.getPortionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Portion not found with id: " + request.getPortionId()));
        }

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

        Portion portion = null;
        if (request.getPortionId() != null) {
            portion = portionRepository.findById(request.getPortionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Portion not found with id: " + request.getPortionId()));
        }

        tracking.setPortion(portion);
        tracking.setQuantity(request.getQuantity());

        Tracking updatedTracking = trackingRepository.save(tracking);
        return trackingMapper.toResponse(updatedTracking);
    }
}
