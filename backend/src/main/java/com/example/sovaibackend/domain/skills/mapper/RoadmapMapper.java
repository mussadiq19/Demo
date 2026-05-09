package com.sovai.platform.domain.skills.mapper;

import com.sovai.platform.domain.skills.dto.RoadmapResponse;
import com.sovai.platform.domain.skills.entity.Roadmap;
import com.sovai.platform.domain.skills.entity.RoadmapStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoadmapMapper {
    @Mapping(target = "roadmapId", source = "id")
    RoadmapResponse toResponse(Roadmap roadmap);

    RoadmapResponse.RoadmapStepResponse toStepResponse(RoadmapStep step);
}

