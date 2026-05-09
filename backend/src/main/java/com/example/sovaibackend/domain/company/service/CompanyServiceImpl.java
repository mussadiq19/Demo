package com.sovai.platform.domain.company.service;

import com.sovai.platform.common.exception.ResourceNotFoundException;
import com.sovai.platform.domain.company.dto.CompanyRequest;
import com.sovai.platform.domain.company.dto.CompanyResponse;
import com.sovai.platform.domain.company.entity.Company;
import com.sovai.platform.domain.company.repository.CompanyRepository;
import com.sovai.platform.domain.risk.entity.RiskStatus;
import com.sovai.platform.domain.risk.repository.RiskRepository;
import com.sovai.platform.domain.skills.repository.EmployeeSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final RiskRepository riskRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

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
    public Map<String, Object> dashboard(Long id) {
        companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Map<String, Object> response = new HashMap<>();
        response.put("openRisks", riskRepository.findByCompanyIdAndStatus(id, RiskStatus.OPEN).size());
        response.put("skillsAssessed", employeeSkillRepository.findAll().size());
        response.put("message", "Insights to strengthen resilience and workforce readiness");
        return response;
    }

    private CompanyResponse map(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getTechStack(),
                company.getSupplyChainInfo()
        );
    }
}

