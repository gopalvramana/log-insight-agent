package com.symphony.logagent.DTO;

public record LogInsightRequest(
        String logs,
        String service,
        String environment
) {}
