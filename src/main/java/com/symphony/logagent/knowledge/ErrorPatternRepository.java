package com.symphony.logagent.knowledge;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ErrorPatternRepository {
    private final Map<String, String> errorPatterns = Map.of(
            "NullPointerException", "Null reference accessed",
            "TimeoutException", "Downstream service latency",
            "SQLException", "Database operation failure"
    );

    public String getDescription(String errorType) {
        return errorPatterns.get(errorType);
    }

    public Map<String, String> getErrorPatterns() {
        return errorPatterns;
    }
}
