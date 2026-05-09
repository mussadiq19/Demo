package com.sovai.platform.infrastructure.ai;

public record AiRequest(
    String model,
    String systemPrompt,
    String userPrompt,
    int maxTokens
) {
}
