package com.symphony.logagent.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.symphony.logagent.builder.PromptBuilder;
import com.symphony.logagent.knowledge.KnowledgeBase;
import com.symphony.logagent.model.LogInsightResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogInsightAgent {
    private final ChatModel chatModel;
    private final PromptBuilder promptBuilder;
    private final KnowledgeBase knowledgeBase;
    private final ObjectMapper objectMapper = new ObjectMapper();
    String rawResponse = "";

    public LogInsightAgent(ChatModel chatModel, PromptBuilder promptBuilder, KnowledgeBase knowledgeBase) {
        this.chatModel = chatModel;
        this.promptBuilder = promptBuilder;
        this.knowledgeBase = knowledgeBase;
    }


    public LogInsightResponse analyze(String logs) {

        if (logs == null || logs.trim().isEmpty()) {
            return LogInsightResponse.fallback("No logs provided");
        }

        try {
        String prompt = promptBuilder.buildPrompt(logs,knowledgeBase.getKnownErrors(), knowledgeBase.getKnownRemediations());
        System.out.println("Generated Prompt: " + prompt);
        rawResponse = chatModel.call(prompt);
        System.out.println("Raw LLM Response: " + rawResponse);
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
