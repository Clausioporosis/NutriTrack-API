package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.nutritrack.model.User;
import com.nutritrack.model.UserStats;
import com.nutritrack.model.UserStatsId;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, UserStatsId> {
    List<UserStats> findByUserId(Long userId);

    Optional<UserStats> findById(UserStatsId id);

    List<UserStats> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate);
}
