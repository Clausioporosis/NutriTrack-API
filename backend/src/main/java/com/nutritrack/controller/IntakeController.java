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

import com.nutritrack.dto.BasicDailyIntakeResponse;
import com.nutritrack.dto.DetailedDailyIntakeResponse;

import java.text.ParseException;
import java.util.List;

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

    @PutMapping("/{intakeId}")
    @Operation(summary = "Update a food intake")
    public ResponseEntity<DailyIntakeResponse> updateFoodIntake(@PathVariable Long intakeId,
            @RequestBody DailyIntakeRequest request) {
        DailyIntake updatedIntake = intakeService.updateFoodIntake(intakeId, request);
        DailyIntakeResponse response = IntakeMapper.toDailyIntakeResponse(updatedIntake);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{intakeId}")
    @Operation(summary = "Delete a food intake")
    public ResponseEntity<Void> deleteFoodIntake(@PathVariable Long intakeId) {
        intakeService.deleteFoodIntake(intakeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get intakes for a specific date")
    public ResponseEntity<List<BasicDailyIntakeResponse>> getIntakesForDate(@PathVariable String date) {
        List<BasicDailyIntakeResponse> intakes = intakeService.getIntakesForDate(date);
        return ResponseEntity.ok(intakes);
    }

    @GetMapping("/{intakeId}")
    @Operation(summary = "Get intake by ID")
    public ResponseEntity<DailyIntakeResponse> getIntakeById(@PathVariable Long intakeId) {
        DailyIntakeResponse intake = intakeService.getIntakeById(intakeId);
        return ResponseEntity.ok(intake);
    }

    @GetMapping
    @Operation(summary = "Get all intakes")
    public ResponseEntity<List<BasicDailyIntakeResponse>> getAllIntakes() {
        List<BasicDailyIntakeResponse> intakes = intakeService.getAllIntakes();
        return ResponseEntity.ok(intakes);
    }
}
