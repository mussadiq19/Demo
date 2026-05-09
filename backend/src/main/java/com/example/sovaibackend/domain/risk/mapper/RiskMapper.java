package com.sovai.platform.domain.risk.mapper;

import com.sovai.platform.domain.risk.dto.RiskResponse;
import com.sovai.platform.domain.risk.entity.Risk;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskMapper {
    RiskResponse toResponse(Risk risk);
}

