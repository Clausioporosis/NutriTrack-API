package com.nutritrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.nutritrack.dto.UserStatsResponse;
import com.nutritrack.model.UserStats;
import com.nutritrack.service.UserStatsService;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@Tag(name = "User Stats Management", description = "APIs for managing user stats")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;

    @GetMapping
    @Operation(summary = "Get user stats")
    public ResponseEntity<List<UserStatsResponse>> getUserStats() {
        List<UserStatsResponse> stats = userStatsService.getUserStats();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/reset")
    @Operation(summary = "Reset user stats")
    public ResponseEntity<Void> resetUserStats() {
        userStatsService.resetUserStats();
        return ResponseEntity.noContent().build();
    }
}
