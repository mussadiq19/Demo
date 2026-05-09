package com.example.sovaibackend.domain.skills.repository;

import com.example.sovaibackend.domain.skills.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
    List<EmployeeSkill> findByUserId(Long userId);
    List<EmployeeSkill> findBySkillId(Long skillId);
    Optional<EmployeeSkill> findByUserIdAndSkillId(Long userId, Long skillId);
    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}

