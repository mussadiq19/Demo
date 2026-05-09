package com.sovai.platform.domain.skills.service;

import com.sovai.platform.domain.auth.entity.User;
import com.sovai.platform.domain.auth.repository.UserRepository;
import com.sovai.platform.domain.skills.dto.GapAnalysisResponse;
import com.sovai.platform.domain.skills.dto.SkillUploadRequest;
import com.sovai.platform.domain.skills.entity.EmployeeSkill;
import com.sovai.platform.domain.skills.entity.ProficiencyLevel;
import com.sovai.platform.domain.skills.entity.Skill;
import com.sovai.platform.domain.skills.repository.EmployeeSkillRepository;
import com.sovai.platform.domain.skills.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillsServiceImpl implements SkillsService {

    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public int uploadSkills(SkillUploadRequest request) {
        int count = 0;
        for (SkillUploadRequest.SkillEntry input : request.skills()) {
            Skill skill = skillRepository.findByName(input.skillName())
                    .orElseGet(() -> skillRepository.save(Skill.builder()
                            .name(input.skillName())
                            .category(input.category())
                            .isEmerging(Boolean.FALSE)
                            .build()));

            EmployeeSkill entity = employeeSkillRepository.findByUserIdAndSkillId(input.userId(), skill.getId())
                    .orElse(EmployeeSkill.builder()
                            .userId(input.userId())
                            .skillId(skill.getId())
                            .build());

            entity.setProficiency(ProficiencyLevel.valueOf(input.proficiency().toUpperCase()));
            employeeSkillRepository.save(entity);
            count++;
        }
        return count;
    }

    @Override
    public List<GapAnalysisResponse> companyGaps(Long companyId) {
        List<User> users = userRepository.findByCompanyId(companyId);
        List<GapAnalysisResponse> output = new ArrayList<>();
        for (User user : users) {
            output.add(employeeGap(user.getId()));
        }
        return output;
    }

    @Override
    public GapAnalysisResponse employeeGap(Long userId) {
        List<EmployeeSkill> skills = employeeSkillRepository.findByUserId(userId);
        boolean lacksAdvanced = skills.stream().noneMatch(s -> s.getProficiency() == ProficiencyLevel.ADVANCED || s.getProficiency() == ProficiencyLevel.EXPERT);
        return new GapAnalysisResponse(
            userId,
            lacksAdvanced ? List.of("Advanced analytical skills", "AI governance basics") : List.of(),
            lacksAdvanced ? "HIGH" : "MEDIUM",
            "Invest in targeted training to improve adaptability and long-term employability."
        );
    }
}

