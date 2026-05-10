package com.example.sovaibackend.domain.company.service;

import com.example.sovaibackend.common.exception.ResourceNotFoundException;
import com.example.sovaibackend.domain.auth.repository.UserRepository;
import com.example.sovaibackend.domain.company.dto.AiReadinessResponse;
import com.example.sovaibackend.domain.company.dto.CompanyRequest;
import com.example.sovaibackend.domain.company.dto.CompanyResponse;
import com.example.sovaibackend.domain.company.dto.DashboardResponse;
import com.example.sovaibackend.domain.company.entity.Company;
import com.example.sovaibackend.domain.company.repository.CompanyRepository;
import com.example.sovaibackend.domain.cache.entity.AiCache;
import com.example.sovaibackend.domain.cache.repository.AiCacheRepository;
import com.example.sovaibackend.domain.risk.entity.Risk;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;
import com.example.sovaibackend.domain.risk.repository.RiskRepository;
import com.example.sovaibackend.domain.skills.repository.EmployeeSkillRepository;
import com.example.sovaibackend.domain.skills.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final RiskRepository riskRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final RoadmapRepository roadmapRepository;
    private final AiCacheRepository aiCacheRepository;
    private final UserRepository userRepository;

    @Override
    public CompanyResponse getById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return map(company);
    }

    @Override
    @Transactional
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        company.setName(request.name());
        company.setIndustry(request.industry());
        company.setTechStack(request.techStack());
        company.setSupplyChainInfo(request.supplyChainInfo());
        return map(companyRepository.save(company));
    }

    @Override
    public DashboardResponse getDashboard(Long companyId) {
        companyRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        int critical = Math.toIntExact(riskRepository.countByCompanyIdAndSeverity(companyId, RiskSeverity.CRITICAL));
        int high = Math.toIntExact(riskRepository.countByCompanyIdAndSeverity(companyId, RiskSeverity.HIGH));
        int gapPct = employeeSkillRepository.calculateGapPercentage(companyId);
        int roadmapPct = roadmapRepository.calculateProgressPercentage(companyId);
        Risk latest = riskRepository.findTopByCompanyIdOrderByDetectedAtDesc(companyId).orElse(null);
        AiCache insightCache = aiCacheRepository.findTopByOrderByCreatedAtDesc().orElse(null);

        return new DashboardResponse(
            critical,
            high,
            "↑ " + critical + " since last scan",
            "↑ " + high + " this week",
            gapPct,
            "↓ vs last month",
            roadmapPct,
            "↑ on track",
            latest != null ? latest.getDetectedAt().toString() : null,
            0,
            insightCache != null ? insightCache.getResponseJson() : null
        );
    }

    @Override
    public AiReadinessResponse calculateAiReadiness(Long companyId) {
        int riskCount = riskRepository.countByCompanyIdAndStatus(companyId, RiskStatus.OPEN);
        int gapPct = 0;
        try {
            Integer g = employeeSkillRepository.calculateGapPercentage(companyId);
            gapPct = g != null ? g : 0;
        } catch (Exception e) {
            gapPct = 0;
        }

        int roadmapPct = 0;
        try {
            Integer r = roadmapRepository.calculateProgressPercentage(companyId);
            roadmapPct = r != null ? r : 0;
        } catch (Exception e) {
            roadmapPct = 0;
        }

        int workforcePreparedness = 100 - gapPct;
        int riskExposureScore = Math.min(riskCount * 10, 100);
        String riskExposure = riskCount == 0 ? "Low"
                : riskCount <= 3 ? "Medium" : "High";
        int overall = (workforcePreparedness + roadmapPct
                + (100 - riskExposureScore)) / 3;

        return new AiReadinessResponse(
                overall, 68, 74,
                workforcePreparedness,
                riskExposure
        );
    }

    private CompanyResponse map(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                formatCompanySize(userRepository.findByCompanyId(company.getId()).size()),
                company.getTechStack(),
                company.getSupplyChainInfo()
        );
    }

    private String formatCompanySize(int count) {
        if (count <= 10) {
            return "1 - 10 Employees";
        }
        if (count <= 50) {
            return "11 - 50 Employees";
        }
        if (count <= 200) {
            return "51 - 200 Employees";
        }
        if (count <= 1000) {
            return "201 - 1000 Employees";
        }
        return "1000+ Employees";
    }
}
