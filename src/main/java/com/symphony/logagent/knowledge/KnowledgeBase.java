package com.symphony.logagent.knowledge;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface KnowledgeBase {
    Map<String, String> getKnownErrors();
    Map<String, String> getKnownRemediations();
    List<String> getKnownErrorPatterns(String exceptionType);
    List<String> getKnownRemediations(String exceptionType);
}
