package com.example.sovaibackend.domain.risk.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "risk_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String icon;
}

