package com.nutritrack.repository;

import com.nutritrack.model.DailyUserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyUserStatsRepository extends JpaRepository<DailyUserStats, Long> {
    List<DailyUserStats> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    DailyUserStats findByUserIdAndDate(Long userId, LocalDate date);
}
