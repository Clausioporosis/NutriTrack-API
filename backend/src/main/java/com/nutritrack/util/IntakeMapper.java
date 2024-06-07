package com.nutritrack.util;

import com.nutritrack.model.DailyIntake;
import com.nutritrack.dto.DailyIntakeResponse;

public class IntakeMapper {
    public static DailyIntakeResponse toDailyIntakeResponse(DailyIntake dailyIntake) {
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
}
