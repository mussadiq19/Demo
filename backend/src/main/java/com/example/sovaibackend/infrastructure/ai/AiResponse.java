package com.sovai.platform.infrastructure.ai;

public record AiResponse(
    String content,
    int inputTokens,
    int outputTokens
) {
}
