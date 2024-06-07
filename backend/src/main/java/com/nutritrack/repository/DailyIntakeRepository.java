package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nutritrack.model.DailyIntake;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface DailyIntakeRepository extends JpaRepository<DailyIntake, Long> {
    List<DailyIntake> findByUserIdAndDate(Long userId, Date date);

    List<DailyIntake> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate);
}