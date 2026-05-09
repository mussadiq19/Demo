package com.example.sovaibackend.domain.skills.dto;

import java.util.List;

public record RoadmapResponse(
    Long roadmapId,
    Long userId,
    String title,
    List<RoadmapStepResponse> steps
) {
    public record RoadmapStepResponse(
        Long id,
        String skillName,
        String description,
        String resourceUrl,
        int estimatedHours,
        int stepOrder,
        boolean completed
    ) {
    }
}

