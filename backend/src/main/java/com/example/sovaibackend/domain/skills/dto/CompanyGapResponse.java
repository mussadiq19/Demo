package com.example.sovaibackend.domain.skills.dto;

import java.util.List;

public record CompanyGapResponse(
    List<DepartmentGapResponse> byDepartment,
    int overallGapPercentage
) {}
