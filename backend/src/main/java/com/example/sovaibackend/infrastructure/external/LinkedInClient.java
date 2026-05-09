package com.sovai.platform.infrastructure.external;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkedInClient {
    public List<String> fetchSkillTrends(String industry) {
        return List.of("Cloud security", "Data literacy", "AI governance");
    }
}

