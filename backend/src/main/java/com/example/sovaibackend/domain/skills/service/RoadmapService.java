package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.domain.skills.dto.RoadmapResponse;

public interface RoadmapService {
    RoadmapResponse generate(Long userId);
    RoadmapResponse getByUser(Long userId);
    RoadmapResponse completeStep(Long stepId);
}

