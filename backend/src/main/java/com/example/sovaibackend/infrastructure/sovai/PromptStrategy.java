package com.sovai.platform.infrastructure.sovai;

public interface PromptStrategy<T> {
    com.sovai.platform.infrastructure.sovai.SovAIRequest build(T input);
}

