package com.example.sovaibackend.domain.risk.service;

import com.example.sovaibackend.common.audit.AuditListener;
import com.example.sovaibackend.domain.company.entity.Company;
import com.example.sovaibackend.domain.company.repository.CompanyRepository;
import com.example.sovaibackend.domain.risk.dto.RiskResponse;
import com.example.sovaibackend.domain.risk.dto.RiskScanResult;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;
import com.example.sovaibackend.domain.risk.dto.RiskRequest;
import com.example.sovaibackend.domain.risk.repository.RiskRepository;
import com.example.sovaibackend.infrastructure.ai.AiClient;
import com.example.sovaibackend.infrastructure.sovai.prompt.RiskScanPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskScannerJobServiceImpl implements RiskScannerJobService, AnalysisJob {

    private final CompanyRepository companyRepository;
    private final AiClient aiClient;
    private final RiskScanPromptBuilder promptBuilder;
    private final RiskService riskService;
    private final RiskRepository riskRepository;
    private final AuditListener auditListener;

    @Override
    public RiskScanResult runScan(Long companyId) {
        auditListener.logStart("RiskScannerJob", companyId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        aiClient.complete(promptBuilder.build(company));

        RiskRequest seed = buildSeed(companyId);
        RiskResponse created = null;
        boolean exists = riskRepository.existsByCompanyIdAndTitleAndStatus(
                companyId,
                seed.title(),
                RiskStatus.OPEN
        );
        if (!exists) {
            created = riskService.create(seed);
        }
        auditListener.logEnd("RiskScannerJob", companyId);

        return new RiskScanResult(
            List.of(new RiskScanResult.RiskItem(
                seed.title(),
                seed.description(),
                seed.severity().name(),
                seed.source(),
                "See risk details for mitigation steps"
            ))
        );
    }

    @Override
    public void run(Long companyId) {
        runScan(companyId);
    }

    private RiskRequest buildSeed(Long companyId) {
        return new RiskRequest(
            companyId,
            null,
            "Vendor concentration risk",
            "Supplier dependency detected. Recommend multi-vendor backup plan and staff cross-training.",
            RiskSeverity.MEDIUM,
            "SUPPLY_CHAIN"
        );
    }
}
