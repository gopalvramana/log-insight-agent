package com.symphony.logagent.builder;

import com.symphony.logagent.DTO.NormalizedLog;
import com.symphony.logagent.repository.ErrorPatternRepository;
import com.symphony.logagent.repository.RemediationRepository;
import com.symphony.logagent.service.KnowledgeService;
import com.symphony.logagent.util.LogNormalizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PromptBuilder {
    private final ErrorPatternRepository errorRepo;
    private final RemediationRepository remediationRepo;
    private final KnowledgeService knowledgeService;
    private final LogNormalizer logNormalizer;

    public PromptBuilder(ErrorPatternRepository errorRepo,
                         RemediationRepository remediationRepo, KnowledgeService knowledgeService, LogNormalizer logNormalizer) {
        this.errorRepo = errorRepo;
        this.remediationRepo = remediationRepo;
        this.knowledgeService = knowledgeService;
        this.logNormalizer = logNormalizer;
    }

    /*public String buildPrompt(String logs) {
        StringBuilder prompt = new StringBuilder();

        // 1. Agent Role
        prompt.append("""
        You are a Log Insight Agent used in production Java microservices.
        You analyze application logs and identify error types, root causes,
        and remediation actions.
        
        """);

        // 2. Operating Rules
        prompt.append("""
        Rules:
        - Use only the provided error patterns and remediation knowledge.
        - Do not invent new error types.
        - If information is insufficient, respond with null values.
        - Be concise and technical.
        
        """);



        // 3. Knowledge: Error Patterns
        prompt.append("Known Error Patterns:\n");
        for (Map.Entry<String, String> entry : knowledgeService.getKnownErrors().entrySet()) {
            List<String> knownErrorList = new ArrayList<>();
            knownErrorList.add(entry.getKey() + ": " + entry.getValue());
            prompt.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        prompt.append("\n");

        // 4. Knowledge: Remediations
        prompt.append("Known Remediations:\n");
        for (Map.Entry<String, String> entry : knowledgeService.getKnownRemediations().entrySet()) {
            List<String> remidiationStepsList = new ArrayList<>();
            remidiationStepsList.add(entry.getKey() + ": " + entry.getValue());
            prompt.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        prompt.append("\n");

        // 5. Logs
        prompt.append("\n\n Analyze the following logs:\n");
        prompt.append(logs).append("\n\n");



        // 6. Output Contract
        prompt.append("""
        Return ONLY valid JSON in the following format.
        Do not include any text outside JSON.
        
        {
          "errorType": "<string or null>",
          "rootCause": "<string or null>",
          "suggestedAction": "<string or null>",
          "confidence": "HIGH | MEDIUM | LOW",
          "explanation": "<short explanation>"
        }
        """);

        return prompt.toString();
    }*/

    public String buildPrompt(String rawLog) {
        StringBuilder prompt = new StringBuilder();

        //get normalized log from raw log
        NormalizedLog normalized = logNormalizer.normalizeLog(rawLog);
        System.out.println("Normalized Log: " + normalized);

        //get exception type string
        String exceptionType = logNormalizer.extractExceptionType(rawLog);
        System.out.println("Extracted Exception Type: " + exceptionType);

        // 1. Agent Role
        prompt.append("""
        You are a Log Insight Agent used in production Java microservices.
        You analyze application logs and identify error types, root causes,
        and remediation actions.
        
        """);

        // 2. Operating Rules
        prompt.append("""
        Rules:
        - Use only the provided error patterns and remediation knowledge.
        - Do not invent new error types.
        - If information is insufficient, respond with null values.
        - Be concise and technical.
        
        """);

        // 3. Logs
        prompt.append("\nEXCEPTION TYPE:\n").append(normalized.exceptionType());
        prompt.append("\n\nMESSAGE:\n").append(normalized.message());

        // 4. Knowledge: Error Patterns
        prompt.append("\n\nKNOWN ERROR PATTERNS:\n");
        List<String> errorPatterns = knowledgeService.getKnownErrorPatterns(exceptionType);
        for (String pattern : errorPatterns) {
            prompt.append("- ").append(pattern).append("\n");
        }
        // 5. Knowledge: Remediations
        prompt.append("\nKNOWN REMEDIATIONS:\n");
        List<String> remediations = knowledgeService.getKnownRemediations(exceptionType);
        for (String remediation : remediations) {
            prompt.append("- ").append(remediation).append("\n\n");
        }

        // 6. Output Contract
        prompt.append("""
        Return ONLY valid JSON in the following format.
        Do not include any text outside JSON.
                {
                  "errorType": "",
                  "rootCause": "",
                  "suggestedAction": "",
                  "confidence": "",
                  "explanation": ""
                }
        """);

        return prompt.toString();
    }
}

