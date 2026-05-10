package com.example.sovaibackend.infrastructure.ai;

public record AiResponse(
    String content,
    int promptTokens,
    int completionTokens
) {}
