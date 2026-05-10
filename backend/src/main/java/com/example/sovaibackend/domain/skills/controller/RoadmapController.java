package com.example.sovaibackend.domain.skills.controller;

import com.example.sovaibackend.common.response.ApiResponse;
import com.example.sovaibackend.common.security.UserPrincipal;
import com.example.sovaibackend.domain.skills.dto.RoadmapResponse;
import com.example.sovaibackend.domain.skills.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/roadmap", "/api/roadmaps"})
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping("/generate/{userId}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    public ApiResponse<RoadmapResponse> generate(@PathVariable Long userId) {
        return ApiResponse.ok(roadmapService.generate(userId), "Roadmap generated to keep workforce future-ready");
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','COMPANY_ADMIN','ADMIN')")
    public ApiResponse<RoadmapResponse> get(@PathVariable Long userId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        boolean isEmployee = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
        if (isEmployee && !principal.getId().equals(userId)) {
            throw new IllegalArgumentException("Employees can view only their own roadmap");
        }
        return ApiResponse.ok(roadmapService.getByUser(userId));
    }

    @GetMapping(params = "userId")
    @PreAuthorize("hasAnyRole('EMPLOYEE','COMPANY_ADMIN','ADMIN')")
    public ApiResponse<RoadmapResponse> getByQueryParam(@RequestParam Long userId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        boolean isEmployee = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
        if (isEmployee && !principal.getId().equals(userId)) {
            throw new IllegalArgumentException("Employees can view only their own roadmap");
        }
        return ApiResponse.ok(roadmapService.getByUser(userId));
    }

    @PutMapping("/steps/{stepId}/complete")
    public ApiResponse<RoadmapResponse> completeStep(@PathVariable Long stepId) {
        return ApiResponse.ok(roadmapService.completeStep(stepId));
    }
}
