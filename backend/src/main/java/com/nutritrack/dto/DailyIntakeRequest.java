package com.nutritrack.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DailyIntakeRequest {
    private Long foodId;
    private Long portionId;
    private Double quantity;
    private String date;
}