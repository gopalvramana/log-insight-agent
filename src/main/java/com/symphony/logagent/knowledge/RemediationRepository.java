package com.symphony.logagent.knowledge;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RemediationRepository {
    private final Map<String, String> remediationSteps = Map.of(
            "NullPointerException", "Check for null references before accessing objects.",
            "TimeoutException", "Increase timeout settings or optimize downstream service performance.",
            "SQLException", "Verify database connectivity and query correctness."
    );

    public String getRemediation(String errorType) {
        return remediationSteps.get(errorType);
    }

    public Map<String, String> getRemediationSteps() {
        return remediationSteps;
    }
}
