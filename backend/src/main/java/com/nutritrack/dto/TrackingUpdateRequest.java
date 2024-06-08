package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TrackingUpdateRequest {
    private Long portionId;
    private float quantity;
}
