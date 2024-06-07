package com.nutritrack.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BasicDailyIntakeResponse {
    private Long intakeId;
    private Long foodId;
    private String title;
    private String brand;
    private String category;
    private Date date;
}
