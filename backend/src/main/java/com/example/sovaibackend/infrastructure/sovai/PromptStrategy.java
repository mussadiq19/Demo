package com.example.sovaibackend.infrastructure.sovai;

public interface PromptStrategy<T> {
    com.example.sovaibackend.infrastructure.sovai.SovAIRequest build(T input);
}

