package com.nutritrack.mapper;

import com.nutritrack.dto.TrackingResponse;
import com.nutritrack.model.Tracking;
import org.springframework.stereotype.Component;

@Component
public class TrackingMapper {

    public TrackingResponse toResponse(Tracking tracking) {
        TrackingResponse response = new TrackingResponse();
        response.setId(tracking.getId());
        response.setFoodId(tracking.getFood().getId());
        response.setFoodTitle(tracking.getFood().getTitle());
        response.setFoodBrand(tracking.getFood().getBrand());
        response.setFoodCategory(tracking.getFood().getCategory());
        response.setIsLiquid(tracking.getFood().isLiquid());
        if (tracking.getPortion() != null) {
            response.setPortionId(tracking.getPortion().getId());
            response.setPortionLabel(tracking.getPortion().getLabel());
            response.setPortionQuantity(tracking.getPortion().getQuantity());
        }
        response.setQuantity(tracking.getQuantity());
        response.setTimestamp(tracking.getTimestamp());
        return response;
    }
}
