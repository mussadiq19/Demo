package com.example.sovaibackend.domain.skills.service;

import com.example.sovaibackend.domain.auth.entity.Role;
import com.example.sovaibackend.domain.auth.entity.User;
import com.example.sovaibackend.domain.auth.repository.UserRepository;
import com.example.sovaibackend.domain.skills.dto.GapAnalysisResponse;
import com.example.sovaibackend.domain.skills.dto.CompanyGapResponse;
import com.example.sovaibackend.domain.skills.dto.DepartmentGapResponse;
import com.example.sovaibackend.domain.skills.dto.SkillUploadRequest;
import com.example.sovaibackend.domain.skills.entity.EmployeeSkill;
import com.example.sovaibackend.domain.skills.entity.ProficiencyLevel;
import com.example.sovaibackend.domain.skills.entity.Skill;
import com.example.sovaibackend.domain.skills.repository.EmployeeSkillRepository;
import com.example.sovaibackend.domain.skills.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillsServiceImpl implements SkillsService {

    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final UserRepository userRepository;
    private final SkillsAnalysisJobService skillsAnalysisJobService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int uploadSkills(SkillUploadRequest request) {
        int count = 0;
        Set<Long> companyIds = new HashSet<>();
        for (SkillUploadRequest.SkillEntry input : request.skills()) {
            saveSkillEntry(input.userId(), input.skillName(), input.category(), input.proficiency());
            userRepository.findById(input.userId())
                    .map(User::getCompanyId)
                    .ifPresent(companyIds::add);
            count++;
        }
        companyIds.forEach(skillsAnalysisJobService::runAnalysis);
        return count;
    }

    @Override
    @Transactional
    public int processUpload(MultipartFile file, Long userId) {
        log.info("Processing upload for userId={}", userId);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                log.warn("Skipping upload for userId={} because CSV is empty", userId);
                return 0;
            }

            User uploader = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            Long companyId = uploader.getCompanyId();
            log.info("Company: {}", companyId);
            log.info("CSV header: {}", headerLine);
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                log.info("Processing row {}: {}", lineCount, line);

                if (line.isBlank()) {
                    log.warn("Skipping row {} because it is blank", lineCount);
                    continue;
                }

                List<String> cols = parseCsvLine(line);
                if (cols.size() < 6) {
                    log.warn("Skipping row {} - not enough columns: {}", lineCount, cols.size());
                    continue;
                }

                String employeeName = cols.get(1).trim();
                String department = cols.get(2).trim();
                String skillName = cols.get(3).trim();
                String currentLevel = cols.get(4).trim();
                String targetLevel = cols.get(5).trim();
                log.info(
                        "Parsed row {}: employeeName={}, department={}, skillName={}, currentLevel={}, targetLevel={}",
                        lineCount,
                        employeeName,
                        department,
                        skillName,
                        currentLevel,
                        targetLevel
                );

                if (employeeName.isBlank() || skillName.isBlank() || currentLevel.isBlank()) {
                    log.warn("Skipping row {} - missing employee, skill, or proficiency", lineCount);
                    continue;
                }

                String email = employeeName.toLowerCase().replace(" ", ".") + "@company.com";
                User employee = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            User user = new User();
                            user.setEmail(email);
                            user.setFullName(employeeName);
                            user.setPasswordHash(passwordEncoder.encode("Welcome@123"));
                            user.setRole(Role.EMPLOYEE);
                            user.setCompanyId(companyId);
                            User saved = userRepository.save(user);
                            log.info("Created employee user: {}", saved.getId());
                            return saved;
                        });

                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(Skill.builder()
                                .name(skillName)
                                .category(department)
                                .isEmerging(Boolean.FALSE)
                                .build()));

                EmployeeSkill employeeSkill = employeeSkillRepository
                        .findByUserIdAndSkillId(employee.getId(), skill.getId())
                        .orElse(EmployeeSkill.builder()
                                .userId(employee.getId())
                                .skillId(skill.getId())
                                .build());

                employeeSkill.setProficiency(parseProficiency(currentLevel));
                employeeSkillRepository.save(employeeSkill);
                log.info("Saved skill: {} for {}", skillName, employeeName);
            }

            log.info("Upload complete. {} rows processed.", lineCount);
            skillsAnalysisJobService.runAnalysis(companyId);
            return lineCount;
        } catch (Exception e) {
            log.error("Upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
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

    @Override
    public CompanyGapResponse getGaps(Long companyId) {
        List<User> employees = userRepository.findByCompanyId(companyId);

        Map<String, List<User>> byDepartment = employees.stream()
            .collect(Collectors.groupingBy(u -> u.getRole().name()));

        List<DepartmentGapResponse> deptGaps = byDepartment.entrySet()
            .stream()
            .map(entry -> {
                String dept = entry.getKey();
                List<Long> userIds = entry.getValue().stream().map(User::getId).toList();

                int gapPct = userIds.isEmpty()
                    ? 0
                    : employeeSkillRepository.calculateGapPercentageForUsers(userIds);

                return new DepartmentGapResponse(dept, gapPct, userIds.size());
            })
            .sorted(Comparator.comparingInt(DepartmentGapResponse::gapPercentage).reversed())
            .toList();

        int overall = employeeSkillRepository.calculateGapPercentage(companyId);

        return new CompanyGapResponse(deptGaps, overall);
    }

    private void saveSkillEntry(Long userId, String skillName, String category, String proficiency) {
        Skill skill = skillRepository.findByName(skillName)
                .orElseGet(() -> skillRepository.save(Skill.builder()
                        .name(skillName)
                        .category(category)
                        .isEmerging(Boolean.FALSE)
                        .build()));

        EmployeeSkill entity = employeeSkillRepository.findByUserIdAndSkillId(userId, skill.getId())
                .orElse(EmployeeSkill.builder()
                        .userId(userId)
                        .skillId(skill.getId())
                        .build());

        entity.setProficiency(ProficiencyLevel.valueOf(proficiency.trim().toUpperCase()));
        employeeSkillRepository.save(entity);
    }

    private ProficiencyLevel parseProficiency(String value) {
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        if (normalized.equals("BASIC") || normalized.equals("NOVICE")) {
            return ProficiencyLevel.BEGINNER;
        }
        if (normalized.equals("MID") || normalized.equals("MEDIUM")) {
            return ProficiencyLevel.INTERMEDIATE;
        }
        if (normalized.equals("PROFICIENT")) {
            return ProficiencyLevel.ADVANCED;
        }
        return ProficiencyLevel.valueOf(normalized);
    }

    private User resolveUser(Map<String, String> row, Map<String, User> usersByEmail, Map<String, User> usersByName) {
        String userId = firstPresent(row, "userid", "user_id", "id");
        if (userId != null && !userId.isBlank()) {
            try {
                return userRepository.findById(Long.parseLong(userId.trim())).orElse(null);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        String email = firstPresent(row, "email", "employeeemail");
        if (email != null && usersByEmail.containsKey(normalize(email))) {
            return usersByEmail.get(normalize(email));
        }
        String name = firstPresent(row, "employeename", "name", "fullname");
        return name != null ? usersByName.get(normalize(name)) : null;
    }

    private String firstPresent(Map<String, String> row, String... keys) {
        for (String key : keys) {
            String value = row.get(key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        values.add(current.toString());
        return values;
    }
}
