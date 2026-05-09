package com.example.sovaibackend.domain.skills.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roadmap_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roadmap_id", nullable = false)
    private Long roadmapId;

    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "estimated_hours")
    private Integer estimatedHours;

    @Column
    private Boolean completed;

    @PrePersist
    protected void onCreate() {
        if (completed == null) {
            completed = false;
        }
    }
}

