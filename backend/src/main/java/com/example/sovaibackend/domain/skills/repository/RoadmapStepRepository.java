package com.example.sovaibackend.domain.skills.repository;

import com.example.sovaibackend.domain.skills.entity.RoadmapStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapStepRepository extends JpaRepository<RoadmapStep, Long> {
    List<RoadmapStep> findByRoadmapIdOrderByStepOrder(Long roadmapId);
}

