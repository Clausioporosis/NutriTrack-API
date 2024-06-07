package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nutritrack.model.Nutrition;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Nutrition findByFoodId(Long foodId);

    void deleteByFoodId(Long foodId);
}
