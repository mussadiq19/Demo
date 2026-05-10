package com.example.sovaibackend.infrastructure.ai;

import com.example.sovaibackend.common.exception.SovAIAdapterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiClient {

    private final WebClient aiWebClient;

    @org.springframework.beans.factory.annotation.Value("${ai.api.model}")
    private String model;

    public AiResponse complete(AiRequest request) {
        try {
            log.debug("Calling AI API with model: {}", model);

            Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", request.maxTokens(),
                "messages", List.of(
                    Map.of("role", "system", "content", request.systemPrompt()),
                    Map.of("role", "user", "content", request.userPrompt())
                )
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = aiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res ->
                    res.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("AI API error: {}", errorBody);
                            return reactor.core.publisher.Mono.error(
                                new SovAIAdapterException("AI API error: " + errorBody)
                            );
                        })
                )
                .bodyToMono(Map.class)
                .block();

            if (response == null) {
                throw new SovAIAdapterException("AI API returned null response");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new SovAIAdapterException("AI API response missing choices");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) {
                throw new SovAIAdapterException("AI API response missing message");
            }

            String text = (String) message.get("content");

            @SuppressWarnings("unchecked")
            Map<String, Object> usage = (Map<String, Object>) response.get("usage");
            if (usage == null) {
                usage = Map.of("prompt_tokens", 0, "completion_tokens", 0);
            }

            int promptTokens = ((Number) usage.getOrDefault("prompt_tokens", 0)).intValue();
            int completionTokens = ((Number) usage.getOrDefault("completion_tokens", 0)).intValue();

            log.info("AI API call succeeded: {} prompt tokens, {} completion tokens", promptTokens, completionTokens);
            return new AiResponse(text, promptTokens, completionTokens);

        } catch (SovAIAdapterException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI client error", e);
            throw new SovAIAdapterException("AI client error: " + e.getMessage(), e);
        }
    }
}

