package com.example.sovaibackend.domain.skills.repository;

import com.example.sovaibackend.domain.skills.entity.Roadmap;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByUserId(Long userId);

    @EntityGraph(attributePaths = {})
    Optional<Roadmap> findWithStepsById(Long id);

    @Query(value = """
        SELECT COALESCE(CAST(
          COUNT(CASE WHEN rs.completed = true THEN 1 END)
          * 100.0 / NULLIF(COUNT(*), 0)
        AS SIGNED), 0)
        FROM roadmap_steps rs
        JOIN roadmaps r ON r.id = rs.roadmap_id
        JOIN users u ON u.id = r.user_id
        WHERE u.company_id = :companyId
        """, nativeQuery = true)
    int calculateProgressPercentage(@Param("companyId") Long companyId);
}

