package com.nutritrack.repository;

import com.nutritrack.model.DailyUserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyUserStatsRepository extends JpaRepository<DailyUserStats, Long> {
    Optional<DailyUserStats> findByUserIdAndDate(Long userId, LocalDate date);
}
