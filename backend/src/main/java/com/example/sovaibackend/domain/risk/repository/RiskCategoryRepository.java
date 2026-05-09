package com.sovai.platform.domain.risk.repository;

import com.sovai.platform.domain.risk.entity.RiskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategory, Long> {
    Optional<RiskCategory> findByName(String name);
}

