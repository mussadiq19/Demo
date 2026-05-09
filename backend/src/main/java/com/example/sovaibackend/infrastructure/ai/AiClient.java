package com.sovai.platform.infrastructure.ai;

import com.sovai.platform.common.exception.SovAIAdapterException;
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

    public com.sovai.platform.infrastructure.ai.AiResponse complete(AiRequest request) {
        try {
            log.debug("Calling AI API with model: {}", request.model());

            Map<String, Object> body = Map.of(
                "model", request.model(),
                "max_tokens", request.maxTokens(),
                "system", request.systemPrompt(),
                "messages", List.of(Map.of("role", "user", "content", request.userPrompt()))
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = aiWebClient.post()
                .uri("/v1/messages")
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
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
            if (content == null || content.isEmpty()) {
                throw new SovAIAdapterException("AI API response missing content");
            }

            String text = (String) content.get(0).get("text");

            @SuppressWarnings("unchecked")
            Map<String, Object> usage = (Map<String, Object>) response.get("usage");
            if (usage == null) {
                usage = Map.of("input_tokens", 0, "output_tokens", 0);
            }

            int inputTokens = ((Number) usage.getOrDefault("input_tokens", 0)).intValue();
            int outputTokens = ((Number) usage.getOrDefault("output_tokens", 0)).intValue();

            log.info("AI API call succeeded: {} input tokens, {} output tokens", inputTokens, outputTokens);
            return new AiResponse(text, inputTokens, outputTokens);

        } catch (SovAIAdapterException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI client error", e);
            throw new SovAIAdapterException("AI client error: " + e.getMessage(), e);
        }
    }
}

