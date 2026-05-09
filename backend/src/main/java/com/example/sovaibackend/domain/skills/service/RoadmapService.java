package com.sovai.platform.domain.skills.service;

import com.sovai.platform.domain.skills.dto.RoadmapResponse;

public interface RoadmapService {
    RoadmapResponse generate(Long userId);
    RoadmapResponse getByUser(Long userId);
    RoadmapResponse completeStep(Long stepId);
}

