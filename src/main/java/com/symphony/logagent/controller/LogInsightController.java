package com.symphony.logagent.controller;

import com.symphony.logagent.model.LogAnalysisRequest;
import com.symphony.logagent.model.LogInsightResponse;
import com.symphony.logagent.service.LLMAgentLogAnalyzerAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent/log")
public class LogInsightController {

    private final LLMAgentLogAnalyzerAgent agent;

    public LogInsightController(LLMAgentLogAnalyzerAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/analyze")
    public LogInsightResponse analyze(@RequestBody LogAnalysisRequest request) {
        return agent.analyze(request.getLogs());
    }
}
