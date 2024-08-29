package com.sparta.business.domain.open_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.business.entity.AIQuestion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AIQuestionService {

    private final WebClient webClient;
    private final String apiKey = "AIzaSyCEAIuXbYJC0MlqJCjOOdQI5MB8PmDXqPA";
    private final AIQuestionRepository aiQuestionRepository;
    private final ObjectMapper objectMapper;

    // Repository를 생성자에 주입
    public AIQuestionService(AIQuestionRepository aiQuestionRepository, ObjectMapper objectMapper) {
        this.webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();
        this.aiQuestionRepository = aiQuestionRepository;
        this.objectMapper = objectMapper;
    }

    public Mono<String> createProductDescription(AiRequestDto requestDto) {
        String apiUrl = "/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;

        // 질문을 DTO에서 추출
        String question = extractAndModifyQuestionFromDto(requestDto);

        return webClient.post()
            .uri(apiUrl)
            .header("Content-Type", "application/json")
            .bodyValue(requestDto)
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> {
                String answer = extractTextFromResponse(response);
                saveAIQuestion(question, answer);
                return answer;
            });
    }

    public String extractAndModifyQuestionFromDto(AiRequestDto requestDto) {
        // AiRequestDto에서 질문 텍스트를 추출하고, 수정하는 메서드
        List<AiRequestDto.Content> contents = requestDto.getContents();
        if (contents != null && !contents.isEmpty()) {
            List<AiRequestDto.Part> parts = contents.get(0).getParts();
            if (parts != null && !parts.isEmpty()) {
                String text = parts.get(0).getText();

                // 텍스트 마지막에 추가 문구 삽입
                text += " 답변을 최대한 간결하게 50자 이하로";

                // 수정된 텍스트를 DTO에 다시 설정
                parts.get(0).setText(text);

                return text;
            }
        }
        throw new IllegalArgumentException("Invalid AI request data");
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            // "candidates" 배열에서 첫 번째 객체의 "content" -> "parts" -> 첫 번째 객체의 "text" 필드를 추출
            return rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();
        } catch (Exception e) {
            log.error("Failed to parse response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    @Transactional
    public AIQuestion saveAIQuestion(String question, String answer) {
        AIQuestion aiQuestion = AIQuestion.builder()
            .question(question)
            .answer(answer)
            .build();

        return aiQuestionRepository.save(aiQuestion);
    }
}
