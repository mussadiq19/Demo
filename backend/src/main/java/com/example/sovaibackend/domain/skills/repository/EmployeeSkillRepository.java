package com.example.sovaibackend.domain.skills.repository;

import com.example.sovaibackend.domain.skills.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
    List<EmployeeSkill> findByUserId(Long userId);
    List<EmployeeSkill> findBySkillId(Long skillId);
    Optional<EmployeeSkill> findByUserIdAndSkillId(Long userId, Long skillId);
    void deleteByUserIdAndSkillId(Long userId, Long skillId);

    @Query(value = """
        SELECT COALESCE(CAST(
          COUNT(CASE WHEN es.proficiency IN ('BEGINNER','INTERMEDIATE') THEN 1 END)
          * 100.0 / NULLIF(COUNT(*), 0)
        AS SIGNED), 0)
        FROM employee_skills es
        JOIN users u ON u.id = es.user_id
        WHERE u.company_id = :companyId
        """, nativeQuery = true)
    int calculateGapPercentage(@Param("companyId") Long companyId);

    @Query(value = """
        SELECT COALESCE(CAST(
          COUNT(CASE WHEN es.proficiency IN ('BEGINNER','INTERMEDIATE') THEN 1 END)
          * 100.0 / NULLIF(COUNT(*), 0)
        AS SIGNED), 0)
        FROM employee_skills es
        WHERE es.user_id IN (:userIds)
        """, nativeQuery = true)
    int calculateGapPercentageForUsers(@Param("userIds") List<Long> userIds);
}

