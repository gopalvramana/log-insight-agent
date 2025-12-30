package com.symphony.logagent.knowledge;

import org.springframework.stereotype.Component;

import java.util.Map;

public interface KnowledgeBase {
    Map<String, String> getKnownErrors();
    Map<String, String> getKnownRemediations();
}
