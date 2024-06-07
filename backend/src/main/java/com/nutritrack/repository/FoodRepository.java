package com.nutritrack.repository;

import com.nutritrack.model.Food;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByUserId(Long userId);

    List<Food> findByCategory(String category);

    List<Food> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);

    Optional<Food> findByIdAndUserId(Long foodId, Long userId);
}