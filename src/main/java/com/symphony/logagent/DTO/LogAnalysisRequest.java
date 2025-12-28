package com.symphony.logagent.DTO;

public record LogAnalysisRequest(
        String logs,
        String service,
        String environment
) {}
