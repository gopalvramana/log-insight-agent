package com.symphony.logagent.DTO;

public record NormalizedLog(
        String exceptionType,
        String message,
        String originalLog
) {
    @Override
    public String toString() {
        return "NormalizedLog{" +
                "exceptionType='" + exceptionType + '\'' +
                ", message='" + message + '\'' +
                ", originalLog='" + originalLog + '\'' +
                '}';
    }
}

