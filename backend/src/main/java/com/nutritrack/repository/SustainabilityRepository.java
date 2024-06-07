package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nutritrack.model.Sustainability;
import org.springframework.stereotype.Repository;

@Repository
public interface SustainabilityRepository extends JpaRepository<Sustainability, Long> {
    Sustainability findByFoodId(Long foodId);

    void deleteByFoodId(Long foodId);
}