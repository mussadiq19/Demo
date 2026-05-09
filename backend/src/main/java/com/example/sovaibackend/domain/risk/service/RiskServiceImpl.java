package com.example.sovaibackend.domain.risk.service;

import com.example.sovaibackend.common.exception.ResourceNotFoundException;
import com.example.sovaibackend.common.response.PagedResponse;
import com.example.sovaibackend.domain.risk.dto.RiskRequest;
import com.example.sovaibackend.domain.risk.dto.RiskResponse;
import com.example.sovaibackend.domain.risk.entity.Risk;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;
import com.example.sovaibackend.domain.risk.mapper.RiskMapper;
import com.example.sovaibackend.domain.risk.repository.RiskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiskServiceImpl implements RiskService {

    private final RiskRepository riskRepository;
    private final RiskMapper riskMapper;

    @Override
    public PagedResponse<RiskResponse> list(Long companyId, RiskStatus status, RiskSeverity severity, int page, int size) {
        Page<Risk> risks;
        if (status != null) {
            risks = riskRepository.findByCompanyIdAndStatus(companyId, status, PageRequest.of(page, size));
        } else if (severity != null) {
            risks = riskRepository.findByCompanyIdAndSeverity(companyId, severity, PageRequest.of(page, size));
        } else {
            risks = riskRepository.findByCompanyId(companyId, PageRequest.of(page, size));
        }
        return PagedResponse.from(risks.map(riskMapper::toResponse));
    }

    @Override
    public RiskResponse getById(Long id) {
        return riskMapper.toResponse(find(id));
    }

    @Override
    @Transactional
    public RiskResponse create(RiskRequest request) {
        Risk risk = Risk.builder()
                .companyId(request.companyId())
                .categoryId(request.categoryId())
                .title(request.title())
                .description(request.description())
                .severity(request.severity())
                .status(RiskStatus.OPEN)
                .source(request.source())
                .detectedAt(Instant.now())
                .build();
        return riskMapper.toResponse(riskRepository.save(risk));
    }

    @Override
    @Transactional
    public RiskResponse acknowledge(Long id) {
        Risk risk = find(id);
        risk.setStatus(RiskStatus.ACKNOWLEDGED);
        return riskMapper.toResponse(riskRepository.save(risk));
    }

    @Override
    @Transactional
    public RiskResponse resolve(Long id) {
        Risk risk = find(id);
        risk.setStatus(RiskStatus.RESOLVED);
        risk.setResolvedAt(Instant.now());
        return riskMapper.toResponse(riskRepository.save(risk));
    }

    @Override
    public Map<String, Object> stats(Long companyId) {
        Map<String, Long> bySeverity = new HashMap<>();
        for (RiskSeverity s : RiskSeverity.values()) {
            bySeverity.put(s.name(), riskRepository.countByCompanyIdAndStatusAndSeverity(companyId, RiskStatus.OPEN, s));
        }
        return Map.of("companyId", companyId, "openBySeverity", bySeverity);
    }

    private Risk find(Long id) {
        return riskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Risk not found"));
    }

    private String getActionLabel(RiskSeverity severity) {
        return switch (severity) {
            case LOW      -> "Monitor — review in monthly report";
            case MEDIUM   -> "Review soon — assign to team lead";
            case HIGH     -> "Act this week — escalate to management";
            case CRITICAL -> "Immediate action required — escalate now";
        };
    }
}
