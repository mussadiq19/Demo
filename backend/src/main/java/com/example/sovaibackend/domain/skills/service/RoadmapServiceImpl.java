package com.sovai.platform.domain.skills.service;

import com.sovai.platform.common.exception.ResourceNotFoundException;
import com.sovai.platform.domain.auth.repository.UserRepository;
import com.sovai.platform.domain.skills.dto.RoadmapResponse;
import com.sovai.platform.domain.skills.entity.Roadmap;
import com.sovai.platform.domain.skills.entity.RoadmapStep;
import com.sovai.platform.domain.skills.repository.EmployeeSkillRepository;
import com.sovai.platform.domain.skills.repository.RoadmapRepository;
import com.sovai.platform.domain.skills.repository.RoadmapStepRepository;
import com.sovai.platform.infrastructure.ai.AiClient;
import com.sovai.platform.infrastructure.sovai.prompt.SkillsGapPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapStepRepository roadmapStepRepository;
    private final AiClient aiClient;
    private final SkillsGapPromptBuilder skillsGapPromptBuilder;
    private final UserRepository userRepository;
    private final EmployeeSkillRepository employeeSkillRepository;

    @Override
    @Transactional
    public RoadmapResponse generate(Long userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var currentSkills = employeeSkillRepository.findByUserId(userId);
        var industryDemands = List.of("Cloud Architecture", "AI Governance", "Data Security");

        aiClient.complete(skillsGapPromptBuilder.build(user, currentSkills, industryDemands));

        Roadmap roadmap = roadmapRepository.save(Roadmap.builder()
                .userId(userId)
                .title("Future-readiness roadmap")
                .generatedByAi(Boolean.TRUE)
                .build());

        roadmapStepRepository.save(RoadmapStep.builder()
                .roadmapId(roadmap.getId())
                .stepOrder(1)
                .description("Complete AI governance fundamentals course")
                .estimatedHours(10)
                .resourceUrl("https://example.com/ai-governance")
                .completed(false)
                .build());

        roadmapStepRepository.save(RoadmapStep.builder()
                .roadmapId(roadmap.getId())
                .stepOrder(2)
                .description("Practice scenario planning with business risk case studies")
                .estimatedHours(8)
                .resourceUrl("https://example.com/scenario-planning")
                .completed(false)
                .build());

        return getRoadmapResponse(roadmap);
    }

    @Override
    public RoadmapResponse getByUser(Long userId) {
        Roadmap roadmap = roadmapRepository.findByUserId(userId).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found for user"));
        return getRoadmapResponse(roadmap);
    }

    @Override
    @Transactional
    public RoadmapResponse completeStep(Long stepId) {
        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap step not found"));
        step.setCompleted(true);
        roadmapStepRepository.save(step);

        Roadmap roadmap = roadmapRepository.findById(step.getRoadmapId())
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap not found"));
        return getRoadmapResponse(roadmap);
    }

    private RoadmapResponse getRoadmapResponse(Roadmap roadmap) {
        List<RoadmapResponse.RoadmapStepResponse> steps = roadmapStepRepository.findByRoadmapIdOrderByStepOrder(roadmap.getId())
                .stream()
                .sorted(Comparator.comparing(RoadmapStep::getStepOrder))
                .map(step -> new RoadmapResponse.RoadmapStepResponse(
                    step.getId(),
                    step.getSkillId() != null ? step.getSkillId().toString() : "TBD",
                    step.getDescription(),
                    step.getResourceUrl(),
                    step.getEstimatedHours() != null ? step.getEstimatedHours() : 0,
                    step.getStepOrder() != null ? step.getStepOrder() : 0,
                    step.getCompleted() != null && step.getCompleted()
                ))
                .toList();

        return new RoadmapResponse(
            roadmap.getId(),
            roadmap.getUserId(),
            roadmap.getTitle(),
            steps
        );
    }
}

