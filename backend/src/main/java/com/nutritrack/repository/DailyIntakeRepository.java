package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nutritrack.model.DailyIntake;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

@Repository
public interface DailyIntakeRepository extends JpaRepository<DailyIntake, Long> {
    List<DailyIntake> findByUserIdAndDate(Long userId, Date date);

    List<DailyIntake> findByUserId(Long userId);

    Optional<DailyIntake> findById(Long intakeId);

    List<DailyIntake> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate);

}
