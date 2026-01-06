package com.symphony.logagent.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.symphony.logagent.DTO.NormalizedLog;
import com.symphony.logagent.builder.PromptBuilder;
import com.symphony.logagent.knowledge.KnowledgeBase;
import com.symphony.logagent.model.LogInsightResponse;
import com.symphony.logagent.util.LogNormalizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogInsightAgentService {
    private final ChatModel chatModel;
    private final PromptBuilder promptBuilder;
    private final KnowledgeService knowledgeService;
    private final LogNormalizer logNormalizer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    String rawResponse = "";

    public LogInsightAgentService(ChatModel chatModel, PromptBuilder promptBuilder,
                                  KnowledgeService knowledgeService, LogNormalizer logNormalizer) {
        this.chatModel = chatModel;
        this.promptBuilder = promptBuilder;
        this.knowledgeService = knowledgeService;
        this.logNormalizer = logNormalizer;
    }


    public LogInsightResponse analyze(String rawLog) {


        if (rawLog == null || rawLog.trim().isEmpty()) {
            return LogInsightResponse.fallback("No logs provided");
        }

        try {
        String prompt = promptBuilder.buildPrompt(rawLog);
        rawResponse = chatModel.call(prompt);
        log.info("Raw LLM Response: {}", rawResponse);
        } catch (Exception ex) {
            log.error("LLM analysis failed", ex);
            return LogInsightResponse.fallback("LLM unavailable. Please retry later.");
            }
        try {
            return objectMapper.readValue(rawResponse, LogInsightResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }
}
