package com.symphony.logagent.knowledge;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryKnowledgeBase implements KnowledgeBase {


    private final Map<String, String> knownErrors = Map.of(
            "NullPointerException", "Accessing a null reference",
            "TimeoutException", "Downstream service timeout"
    );

    private final Map<String, String> knownRemediations = Map.of(
            "NullPointerException", "Check null checks and input validation",
            "TimeoutException", "Increase timeout or investigate downstream latency"
    );

    @Override
    public Map<String, String> getKnownErrors() {
        // Implementation here
        return knownErrors;
    }

    @Override
    public Map<String, String> getKnownRemediations() {
        // Implementation here
        return knownRemediations;
    }
}
