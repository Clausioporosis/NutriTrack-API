package com.nutritrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.nutritrack.model.Portion;

@Repository
public interface PortionRepository extends JpaRepository<Portion, Long> {
    List<Portion> findByFoodId(Long foodId);

    void deleteByFoodId(Long foodId);
}
