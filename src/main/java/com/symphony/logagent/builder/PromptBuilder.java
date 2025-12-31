package com.symphony.logagent.builder;

import com.symphony.logagent.DTO.NormalizedLog;
import com.symphony.logagent.knowledge.DbKnowledgeBase;
import com.symphony.logagent.knowledge.KnowledgeBase;
import com.symphony.logagent.repository.ErrorPatternRepository;
import com.symphony.logagent.repository.RemediationRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PromptBuilder {
    private final ErrorPatternRepository errorRepo;
    private final RemediationRepository remediationRepo;
    private final DbKnowledgeBase knowledgeBase;

    public PromptBuilder(ErrorPatternRepository errorRepo,
                         RemediationRepository remediationRepo, DbKnowledgeBase knowledgeBase) {
        this.errorRepo = errorRepo;
        this.remediationRepo = remediationRepo;
        this.knowledgeBase = knowledgeBase;
    }

    public String buildPrompt(String logs) {
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
/*        errorRepo.getErrorPatterns().forEach((error, description) -> {
            prompt.append("- ")
                    .append(error)
                    .append(": ")
                    .append(description)
                    .append("\n");
        });*/
        for (Map.Entry<String, String> entry : knowledgeBase.getKnownErrors().entrySet()) {
            prompt.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        prompt.append("\n");

        // 4. Knowledge: Remediations
        prompt.append("Known Remediations:\n");
/*        remediationRepo.getRemediationSteps().forEach((error, remediation) -> {
            prompt.append("- ")
                    .append(error)
                    .append(": ")
                    .append(remediation)
                    .append("\n");
        });*/
        for (Map.Entry<String, String> entry : knowledgeBase.getKnownRemediations().entrySet()) {
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
    }

    public String buildPrompt(NormalizedLog logs, Map<String, String> knownErrors,
                              Map<String, String> knownRemediations) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
        You are a production incident analysis agent.
        Analyze the log and provide:
        - Root cause
        - Explanation
        - Suggested remediation
        """);

        prompt.append("\nEXCEPTION TYPE:\n").append(logs.exceptionType());
        prompt.append("\n\nMESSAGE:\n").append(logs.message());
        prompt.append("\n\nKNOWN ERROR PATTERNS:\n");
        knownErrors.forEach((k, v) -> prompt.append("- ").append(k).append(": ").append(v).append("\n"));
        prompt.append("\nKNOWN REMEDIATIONS:\n");
        knownRemediations.forEach((k, v) -> prompt.append("- ").append(k).append(": ").append(v).append("\n"));

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