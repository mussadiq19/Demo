package com.example.sovaibackend.domain.risk.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.common.response.PagedResponse;
import com.example.sovaibackend.domain.risk.dto.RiskResponse;
import com.example.sovaibackend.domain.risk.dto.RiskScanResult;
import com.example.sovaibackend.domain.risk.entity.RiskSeverity;
import com.example.sovaibackend.domain.risk.entity.RiskStatus;
import com.example.sovaibackend.domain.risk.service.RiskScannerJobService;
import com.example.sovaibackend.domain.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/risks")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;
    private final RiskScannerJobService riskScannerJobService;

    @GetMapping
    public ApiResponse<PagedResponse<RiskResponse>> list(
            @RequestParam Long companyId,
            @RequestParam(required = false) RiskStatus status,
            @RequestParam(required = false) RiskSeverity severity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(riskService.list(companyId, status, severity, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<RiskResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(riskService.getById(id));
    }

    @PostMapping("/scan")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ApiResponse<RiskScanResult> scan(
            @RequestBody(required = false) Map<String, Long> body,
            @RequestParam(required = false) Long companyId) {
        Long targetCompanyId = companyId != null ? companyId : body != null ? body.get("companyId") : null;
        if (targetCompanyId == null) {
            throw new IllegalArgumentException("companyId is required");
        }
        return ApiResponse.ok(riskScannerJobService.runScan(targetCompanyId), "Risk scan triggered to support proactive decisions");
    }

    @PutMapping("/{id}/acknowledge")
    public ApiResponse<RiskResponse> acknowledge(@PathVariable Long id) {
        return ApiResponse.ok(riskService.acknowledge(id));
    }

    @PutMapping("/{id}/resolve")
    public ApiResponse<RiskResponse> resolve(@PathVariable Long id) {
        return ApiResponse.ok(riskService.resolve(id));
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> stats(@RequestParam Long companyId) {
        return ApiResponse.ok(riskService.stats(companyId));
    }
}
