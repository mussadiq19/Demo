package com.example.sovaibackend.domain.skills.dto;

public record DepartmentGapResponse(
    String name,
    int gapPercentage,
    int staffCount
) {}
