package com.symphony.logagent.controller;

import com.symphony.logagent.model.LogInsightRequest;
import com.symphony.logagent.model.LogInsightResponse;
import com.symphony.logagent.service.LogInsightAgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent/log")
@Slf4j
public class LogInsightController {

    private final LogInsightAgentService agent;

    public LogInsightController(LogInsightAgentService agent) {
        this.agent = agent;
    }

    @PostMapping("/analyze")
    public LogInsightResponse analyze(@RequestBody LogInsightRequest request) {
        log.info("Received log analysis request....");
        LogInsightResponse response = agent.analyze(request.getLogs());
        log.info("Log analysis completed.....");
        return response;
    }
}
