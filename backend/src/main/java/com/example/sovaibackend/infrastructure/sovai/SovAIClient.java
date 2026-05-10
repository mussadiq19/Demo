package com.example.sovaibackend.infrastructure.sovai;

import com.example.sovaibackend.common.exception.SovAIAdapterException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

public class SovAIClient {

    private WebClient webClient;

    @Value("${sovai.api.base-url}")
    private String baseUrl;

    @Value("${sovai.api.api-key}")
    private String apiKey;

    @Value("${sovai.api.timeout-seconds:30}")
    private int timeoutSeconds;

    public com.example.sovaibackend.infrastructure.sovai.SovAIResponse complete(SovAIRequest request) {
        try {
            Map<String, Object> payload = Map.of(
                    "model", request.model(),
                    "systemPrompt", request.systemPrompt(),
                    "userPrompt", request.userPrompt(),
                    "maxTokens", request.maxTokens()
            );

            String response = webClient.post()
                    .uri(baseUrl + "/v1/complete")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();

            return new SovAIResponse(response == null ? "{}" : response, false);
        } catch (Exception ex) {
            throw new SovAIAdapterException("SovAI completion failed", ex);
        }
    }

    public <T> SovAIResponse complete(PromptStrategy<T> strategy, T input) {
        return complete(strategy.build(input));
    }
}
