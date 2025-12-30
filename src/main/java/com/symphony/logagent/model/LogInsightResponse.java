package com.symphony.logagent.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class LogInsightResponse {
    String errorType;
    String rootCause;
    String confidence;
    String suggestedAction;
    String explanation;

    public static LogInsightResponse fallback(String message) {
        LogInsightResponse response = new LogInsightResponse();
        response.setErrorType("LLM Unavailable");
        response.setRootCause(message);
        response.setConfidence("N/A");
        response.setSuggestedAction("Please retry later.");
        response.setExplanation("The language model is currently unavailable.");
        return response;
    }

}
