package com.sovai.platform.domain.skills.dto;

public record SkillResponse(
    Long id,
    String name,
    String category,
    Boolean isEmerging
) {
}

