package com.example.sovaibackend.domain.risk.mapper;

import com.example.sovaibackend.domain.risk.dto.RiskResponse;
import com.example.sovaibackend.domain.risk.entity.Risk;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskMapper {
    RiskResponse toResponse(Risk risk);
}

