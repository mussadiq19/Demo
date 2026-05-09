package com.sovai.platform.domain.risk.service;

import com.sovai.platform.common.audit.AuditListener;
import com.sovai.platform.domain.company.entity.Company;
import com.sovai.platform.domain.company.repository.CompanyRepository;
import com.sovai.platform.domain.risk.dto.RiskResponse;
import com.sovai.platform.domain.risk.dto.RiskScanResult;
import com.sovai.platform.domain.risk.entity.RiskSeverity;
import com.sovai.platform.domain.risk.dto.RiskRequest;
import com.sovai.platform.infrastructure.ai.AiClient;
import com.sovai.platform.infrastructure.sovai.prompt.RiskScanPromptBuilder;
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
    private final AuditListener auditListener;

    @Override
    public RiskScanResult runScan(Long companyId) {
        auditListener.logStart("RiskScannerJob", companyId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        aiClient.complete(promptBuilder.build(company));

        RiskResponse created = riskService.create(buildSeed(companyId));
        auditListener.logEnd("RiskScannerJob", companyId);

        return new RiskScanResult(
            List.of(new RiskScanResult.RiskItem(
                created.title(),
                created.description(),
                created.severity().name(),
                created.source(),
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

