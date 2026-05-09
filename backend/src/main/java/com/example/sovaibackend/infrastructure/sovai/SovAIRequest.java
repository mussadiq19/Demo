package com.sovai.platform.infrastructure.sovai;

public record SovAIRequest(
    String model,
    String systemPrompt,
    String userPrompt,
    int maxTokens
) {
}

