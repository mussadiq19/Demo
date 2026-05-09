package com.example.sovaibackend.domain.skills.repository;

import com.example.sovaibackend.domain.skills.entity.Roadmap;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByUserId(Long userId);

    @EntityGraph(attributePaths = {})
    Optional<Roadmap> findWithStepsById(Long id);
}

