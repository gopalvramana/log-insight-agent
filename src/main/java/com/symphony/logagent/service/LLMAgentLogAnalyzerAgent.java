package com.symphony.logagent.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.symphony.logagent.builder.PromptBuilder;
import com.symphony.logagent.model.LogInsightResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class LLMAgentLogAnalyzerAgent {
    private final ChatModel chatModel;
    private final PromptBuilder promptBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public LLMAgentLogAnalyzerAgent(ChatModel chatModel, PromptBuilder promptBuilder) {
        this.chatModel = chatModel;
        this.promptBuilder = promptBuilder;
    }
    public LogInsightResponse analyze(String logs) {
        String prompt = promptBuilder.buildPrompt(logs);
        System.out.println("Generated Prompt: " + prompt);
        String rawResponse = chatModel.call(prompt);
        System.out.println("Raw LLM Response: " + rawResponse);

        try {
            return objectMapper.readValue(rawResponse, LogInsightResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }
}
