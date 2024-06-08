package com.nutritrack.repository;

import com.nutritrack.model.Portion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortionRepository extends JpaRepository<Portion, Long> {
    void deleteById(Long id);
}
