package com.nutritrack.controller;

import com.nutritrack.dto.DailyTrackingSummary;
import com.nutritrack.service.DailyUserStatsService;
import com.nutritrack.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
public class DailyUserStatsController {

    @Autowired
    private DailyUserStatsService dailyUserStatsService;

    @Autowired
    private SecurityUtil securityUtil;

    @Operation(summary = "Get daily summary for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily summary retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Stats not found")
    })
    @GetMapping("/daily")
    public ResponseEntity<DailyTrackingSummary.DailySummary> getDailySummary(@RequestParam LocalDate date) {
        Long userId = securityUtil.getUserIdFromToken();
        DailyTrackingSummary.DailySummary dailySummary = dailyUserStatsService.getDailySummary(userId, date);
        return ResponseEntity.ok(dailySummary);
    }
}
