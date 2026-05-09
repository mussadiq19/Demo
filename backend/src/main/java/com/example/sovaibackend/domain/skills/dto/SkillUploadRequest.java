package com.sovai.platform.domain.skills.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SkillUploadRequest(
    @NotNull
    List<SkillEntry> skills
) {
    public record SkillEntry(
        @NotNull
        Long userId,

        @NotBlank
        String skillName,

        String category,

        @NotBlank
        String proficiency
    ) {
    }
}

