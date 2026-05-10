package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.common.audit.AuditListener;
import com.example.sovaibackend.domain.auth.entity.User;
import com.example.sovaibackend.domain.auth.repository.UserRepository;
import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.entity.ProficiencyLevel;
import com.example.sovaibackend.domain.skills.repository.EmployeeSkillRepository;
import com.example.sovaibackend.domain.skills.repository.RoadmapRepository;
import com.example.sovaibackend.domain.skills.repository.RoadmapStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillsAnalysisJobServiceImpl implements SkillsAnalysisJobService, com.example.sovaibackend.domain.risk.service.AnalysisJob {

    private final AuditListener auditListener;
    private final UserRepository userRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final RoadmapRepository roadmapRepository;
    private final RoadmapStepRepository roadmapStepRepository;
    private final RoadmapService roadmapService;

    @Override
    public List<GapAnalysisResponse> runCompanyAnalysis(Long companyId) {
        auditListener.logStart("SkillsAnalysisJob", companyId);
        List<GapAnalysisResponse> response = companyGaps(companyId);
        auditListener.logEnd("SkillsAnalysisJob", companyId);
        return response;
    }

    @Override
    public void runAnalysis(Long companyId) {
        runCompanyAnalysis(companyId);
        userRepository.findByCompanyId(companyId).stream()
                .filter(user -> !employeeSkillRepository.findByUserId(user.getId()).isEmpty())
                .filter(this::needsRoadmap)
                .forEach(user -> roadmapService.generate(user.getId()));
    }

    @Override
    public void run(Long companyId) {
        runAnalysis(companyId);
    }

    private List<GapAnalysisResponse> companyGaps(Long companyId) {
        List<GapAnalysisResponse> output = new ArrayList<>();
        for (User user : userRepository.findByCompanyId(companyId)) {
            var skills = employeeSkillRepository.findByUserId(user.getId());
            boolean lacksAdvanced = skills.stream().noneMatch(s ->
                    s.getProficiency() == ProficiencyLevel.ADVANCED
                            || s.getProficiency() == ProficiencyLevel.EXPERT);
            output.add(new GapAnalysisResponse(
                    user.getId(),
                    lacksAdvanced ? List.of("Advanced analytical skills", "AI governance basics") : List.of(),
                    lacksAdvanced ? "HIGH" : "MEDIUM",
                    "Invest in targeted training to improve adaptability and long-term employability."
            ));
        }
        return output;
    }

    private boolean needsRoadmap(User user) {
        var roadmaps = roadmapRepository.findByUserId(user.getId());
        return roadmaps.isEmpty() || roadmaps.stream()
                .allMatch(roadmap -> roadmapStepRepository.findByRoadmapIdOrderByStepOrder(roadmap.getId()).isEmpty());
    }
}
