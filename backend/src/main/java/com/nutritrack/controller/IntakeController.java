package com.nutritrack.controller;

import com.nutritrack.util.IntakeMapper;
import com.nutritrack.model.DailyIntake;
import com.nutritrack.dto.DailyIntakeRequest;
import com.nutritrack.dto.DailyIntakeResponse;
import com.nutritrack.service.IntakeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/intake")
@Tag(name = "Intake Management", description = "APIs for managing daily intake")
public class IntakeController {

    @Autowired
    private IntakeService intakeService;

    @PostMapping
    @Operation(summary = "Track a food intake")
    public ResponseEntity<DailyIntakeResponse> trackFoodIntake(@RequestBody DailyIntakeRequest request) {
        DailyIntake dailyIntake = intakeService.trackFoodIntake(request);
        DailyIntakeResponse response = IntakeMapper.toDailyIntakeResponse(dailyIntake);
        return ResponseEntity.ok(response);
    }
}