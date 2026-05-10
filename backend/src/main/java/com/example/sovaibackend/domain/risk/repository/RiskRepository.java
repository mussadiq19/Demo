package com.example.sovaibackend.domain.risk.repository;

import com.example.sovaibackend.domain.risk.entity.Risk;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
    Page<Risk> findByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT r FROM Risk r WHERE r.companyId = :companyId AND r.status = :status")
    Page<Risk> findByCompanyIdAndStatus(@Param("companyId") Long companyId,
                                        @Param("status") RiskStatus status,
                                        Pageable pageable);

    @Query("SELECT r FROM Risk r WHERE r.companyId = :companyId AND r.severity = :severity")
    Page<Risk> findByCompanyIdAndSeverity(@Param("companyId") Long companyId,
                                          @Param("severity") RiskSeverity severity,
                                          Pageable pageable);

    @Query("SELECT COUNT(r) FROM Risk r WHERE r.companyId = :companyId AND r.status = :status AND r.severity = :severity")
    long countByCompanyIdAndStatusAndSeverity(@Param("companyId") Long companyId,
                                              @Param("status") RiskStatus status,
                                              @Param("severity") RiskSeverity severity);

    long countByCompanyIdAndSeverity(Long companyId, RiskSeverity severity);

    Optional<Risk> findTopByCompanyIdOrderByDetectedAtDesc(Long companyId);

    List<Risk> findByCompanyIdAndStatus(Long companyId, RiskStatus status);
}

