package com.symphony.logagent.util;

import com.symphony.logagent.DTO.NormalizedLog;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogNormalizer {
    private static final Pattern EXCEPTION_PATTERN =
            Pattern.compile("((?:[a-zA-Z_$][\\w$]*\\.)*(?:[A-Z][\\w$]*)(Exception|Error))");

    public NormalizedLog normalizeLog(String rawLog) {
        System.out.println("Normalizing log: " + rawLog);
        if (rawLog == null || rawLog.isBlank()) {
            return new NormalizedLog("UNKNOWN", "", rawLog);
        }

        Matcher matcher = EXCEPTION_PATTERN.matcher(rawLog);

        String exceptionType = matcher.find() ? matcher.group(1) : "UNKNOWN";

        // Simple cleanup (Phase 1)
        String cleanedMessage = rawLog
                .replaceAll("\\d{4}-\\d{2}-\\d{2}.*?\\s", "") // timestamp
                .replaceAll("\\[.*?\\]", "")                // thread
                .replaceAll("ERROR|WARN|INFO|DEBUG", "")
                .trim();

        return new NormalizedLog(exceptionType, cleanedMessage, rawLog);



    }
}
