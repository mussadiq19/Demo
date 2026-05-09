package com.example.sovaibackend.domain.skills.mapper;

import com.example.sovaibackend.domain.skills.dto.RoadmapResponse;
import com.example.sovaibackend.domain.skills.entity.Roadmap;
import com.example.sovaibackend.domain.skills.entity.RoadmapStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoadmapMapper {
    @Mapping(target = "roadmapId", source = "id")
    RoadmapResponse toResponse(Roadmap roadmap);

    RoadmapResponse.RoadmapStepResponse toStepResponse(RoadmapStep step);
}

