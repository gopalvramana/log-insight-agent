package com.symphony.logagent.util;

import com.symphony.logagent.DTO.NormalizedLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LogNormalizer {
    private static final Pattern EXCEPTION_PATTERN =
            Pattern.compile("((?:[a-zA-Z_$][\\w$]*\\.)*(?:[A-Z][\\w$]*)(Exception|Error))");

    // Normalizes a raw log message into a structured format
    public NormalizedLog normalizeLog(String rawLog) {
        System.out.println("Normalizing log: " + rawLog);
        if (rawLog == null || rawLog.isBlank()) {
            return new NormalizedLog("UNKNOWN", "", rawLog);
        }

        Matcher matcher = EXCEPTION_PATTERN.matcher(rawLog);

        String exceptionType = matcher.find() ? matcher.group(1) : "UNKNOWN";
        log.info("Extracted exception type: {}", exceptionType);


        // Simple cleanup (Phase 1)
        String cleanedMessage = rawLog
                .replaceAll("\\d{4}-\\d{2}-\\d{2}.*?\\s", "") // timestamp
                .replaceAll("\\[.*?\\]", "")                // thread
                .replaceAll("ERROR|WARN|INFO|DEBUG", "")
                .trim();

        return new NormalizedLog(exceptionType, cleanedMessage, rawLog);
    }

    // Extracts the exception type from a log message
    public String extractExceptionType(String logMessage) {
        Matcher matcher = EXCEPTION_PATTERN.matcher(logMessage);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
}
